package com.androidkotlincore.mvp.impl

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.androidkotlincore.mvp.*
import com.androidkotlincore.mvp.addons.CompositeEventListener
import com.androidkotlincore.mvp.addons.EmitableCompositeEventListener
import com.androidkotlincore.mvp.addons.impl.BehaviourCompositeEventListener
import com.androidkotlincore.mvp.impl.MVPLogger.log
import com.androidkotlincore.mvp.impl.permissions.OnRequestPermissionsResultEvent

/**
 * Created by Peter on 07.01.2017.
 */

/**
 * MVPDelegate for Activities & Fragments which must implement the [LifecycleOwner] interface
 * The following methods must be invoked from the corresponding Activities/Fragments methods:
 * [onActivityResult]
 * [onRequestPermissionsResult]
 *
 * @param presentersStorage - storage for presenters. [AbstractMVPDelegate] handles putting [TPresenter]
 * into [presentersStorage] and removing it from [presentersStorage]
 * @property view - [MVPView] with witch this delegate works
 * @property viewPersistenceStorage - [Bundle] from view with key-value pairs
 * @property presenterId - initial presenter id
 */
abstract class AbstractMVPDelegate<TPresenter, TView>(private val presentersStorage: PresentersStorage<TPresenter, TView>) :
        GenericLifecycleObserver,
        MVPView<TView, TPresenter>

        where TPresenter : MVPPresenter<TPresenter, TView>,
              TView : MVPView<TView, TPresenter> {

    constructor() : this(PresentersStorageImpl())

    //View
    private lateinit var view: MVPView<TView, TPresenter>
    protected val viewPersistenceStorage: Bundle
        get() {
            val localView = view
            return if (localView is ViewPersistenceStorage) localView.persistenceArguments
            else throw IllegalStateException("$localView must implement ${ViewPersistenceStorage::class.java.name}")
        }

    private var presenterId: Long = -1L

    /**
     * This function should be called in [TView] init block
     * @param view - view witch uses this delegate
     * */
    protected fun init(view: MVPView<TView, TPresenter>) {
        this.view = view
        if (view is LifecycleOwner) view.getLifecycle().addObserver(this)
        else throw IllegalStateException("$view must implement ${LifecycleOwner::class.java.name} interface!")
    }

    ///////////////////////////////////// MVPView methods overriding ///////////////////////////////
    /**
     * Lazy creation of [TPresenter]
     * */
    override val presenter: TPresenter by lazy { createOrRestorePresenter() }
    /**
     * [BehaviourCompositeEventListener] to emit [Lifecycle.Event]
     * */
    protected val lifecycleEmitter: EmitableCompositeEventListener<Lifecycle.Event> = BehaviourCompositeEventListener()
    /**
     * Use this listener to subscribe on [Lifecycle.Event]
     * */
    override val lifecycle: CompositeEventListener<Lifecycle.Event> = lifecycleEmitter

    /**
     * [BehaviourCompositeEventListener] to emit [OnActivityResultEvent]
     * */
    protected val onActivityResultEmitter: EmitableCompositeEventListener<OnActivityResultEvent> = BehaviourCompositeEventListener()
    /**
     * Use this listener to subscribe on [OnActivityResultEvent]
     * */
    override val onActivityResult: CompositeEventListener<OnActivityResultEvent> = onActivityResultEmitter

    /**
     * [BehaviourCompositeEventListener] to emit [OnRequestPermissionsResultEvent]
     * */
    protected val onRequestPermissionResultEmitter: EmitableCompositeEventListener<OnRequestPermissionsResultEvent> = BehaviourCompositeEventListener()
    /**
     * Use this listener to subscribe on [OnRequestPermissionsResultEvent]
     * */
    override val onRequestPermissionResult: CompositeEventListener<OnRequestPermissionsResultEvent> = onRequestPermissionResultEmitter

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Activity result handler
     * */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResultEmitter.emit(OnActivityResultEvent(requestCode, resultCode, data))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Request permissions callback
     *
     * @param requestCode - permissions request code
     * @param permissions - list of requested permissions
     * @param grantResults - the grant results for the corresponding permissions
     * */
    abstract fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @see [GenericLifecycleObserver.onStateChanged]
     * */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        log("#STATE_CHANGED# ${view.javaClass.name} -> $event")
        presenter //presenter must be initialized here

        when (event) {
            Lifecycle.Event.ON_START -> attachViewToPresenter()
            Lifecycle.Event.ON_STOP -> detachViewFromPresenter()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
            else -> {
                //do nothing
            }
        }

        if (event != Lifecycle.Event.ON_ANY) {
            lifecycleEmitter.emit(event)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun createPresenter(view: MVPView<TView, TPresenter>) = provideInjector(view).createPresenter(view.javaClass, view.mvpTag) as TPresenter

    /**
     * Searches for the [MVPInjector] among candidates:
     * - [MVPView]
     * - [MVPView][contextNotNull]
     * - [Context] of an application
     * Application should implement [MVPInjector]
     * */
    protected open fun provideInjector(view: MVPView<TView, TPresenter>) =
            view as? MVPInjector ?:
                    view.contextNotNull as? MVPInjector ?:
                    view.contextNotNull.applicationContext as? MVPInjector ?:
                    throw IllegalStateException("Implementation of ${MVPInjector::class.java.name} not found!")

    private fun createOrRestorePresenter(): TPresenter {
        val presenterExtraId = "${view.javaClass.name}.presenterId"
        val localPresenterId = viewPersistenceStorage.getLong(presenterExtraId, PRESENTER_NOT_FOUND)

        if (localPresenterId == PRESENTER_NOT_FOUND) {
            presenterId = presentersStorage.generateId()
            viewPersistenceStorage.putLong(presenterExtraId, presenterId)
        } else presenterId = localPresenterId

        var localPresenter = presentersStorage.get(view, presenterId)
        if (localPresenter == null) {
            val isFirstPresenterCreation = localPresenterId == PRESENTER_NOT_FOUND
            if (!isFirstPresenterCreation) {
                //App was killed
                log("View has presenterID ($presenterId), but presenter not found!")
            }

            localPresenter = createPresenter(view)
            log("#CREATING_PRESENTER# presenterId:$presenterId ${localPresenter.javaClass.name} -> " +
                    "${view.javaClass.name}; " +
                    "ifFirstPresenterCreation: $isFirstPresenterCreation")
            presentersStorage.put(view, presenterId, localPresenter)
            localPresenter.onCreated(isFirstPresenterCreation)
        } else log("#REUSING_PRESENTER# presenterId:$presenterId ${localPresenter.javaClass.name} -> ${view.javaClass.name}")

        return localPresenter
    }

    private fun attachViewToPresenter() {
        @Suppress("UNCHECKED_CAST")
        presenter.attachView(view as TView)
        log("#ATTACH_VIEW# ${view.javaClass.name} -> ${presenter.javaClass.name}")
    }

    private fun detachViewFromPresenter() {
        @Suppress("UNCHECKED_CAST")
        presenter.detachView(view as TView)
        log("#DETACH_VIEW# ${view.javaClass.name} -> ${presenter.javaClass.name}")
    }

    private fun onDestroy() {
        if (!retainPresenterInstance()) {
            destroyPresenter()
        }
    }

    /**
     * Predicate which indicates whether presenter should be destroyed when view is destroyed or not
     * @return true to left presenter in storage
     * */
    abstract fun retainPresenterInstance(): Boolean

    private fun destroyPresenter() {
        log("#DESTROY_PRESENTER# presenterId:$presenterId; ${view.javaClass.name} -> ${presenter.javaClass.name}")
        presenter.onDestroyed()
        presentersStorage.remove(view, presenterId)
    }

    private companion object {
        const val PRESENTER_NOT_FOUND = -1L
    }
}