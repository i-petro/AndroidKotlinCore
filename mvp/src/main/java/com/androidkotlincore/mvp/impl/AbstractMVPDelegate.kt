package com.androidkotlincore.mvp.impl

import android.app.Fragment
import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.androidkotlincore.mvp.ViewPersistenceStorage
import com.androidkotlincore.mvp.*
import com.androidkotlincore.mvp.addons.CompositeEventListener
import com.androidkotlincore.mvp.addons.impl.BehaviourCompositeEventListener
import com.androidkotlincore.mvp.impl.MVPLogger.log
import com.androidkotlincore.mvp.impl.permissions.OnRequestPermissionsResultEvent

/**
* Created by Peter on 07.01.2017.
*/

typealias SupportFragment = android.support.v4.app.Fragment

/**
 * MVPDelegate for Activities & Fragments which must implement the [LifecycleOwner] interface
 * The following methods must be invoked from the corresponding Activities/Fragments methods:
 * [onActivityResult]
 * [onRequestPermissionsResult]
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
            return if (localView is ViewPersistenceStorage) localView.args
            else throw IllegalStateException("$localView must implement ${ViewPersistenceStorage::class.java.name}")
        }

    private var presenterId: Long = -1L

    protected fun init(view: MVPView<TView, TPresenter>) {
        this.view = view
        if (view is LifecycleOwner) view.getLifecycle().addObserver(this)
        else throw IllegalStateException("$view must implement ${LifecycleOwner::class.java.name} interface!")
    }

    ///////////////////////////////////// MVPView methods overriding ///////////////////////////////
    override val presenter: TPresenter by lazy { createOrRestorePresenter() }
    override val lifecycle: CompositeEventListener<Lifecycle.Event> = BehaviourCompositeEventListener()
    override val onActivityResult: CompositeEventListener<OnActivityResultEvent> = BehaviourCompositeEventListener()
    override val onRequestPermissionResult: CompositeEventListener<OnRequestPermissionsResultEvent> = BehaviourCompositeEventListener()
    override val contextNotNull: Context
        get() {
            val localView = view
            return when (localView) {
                is Context -> localView
                is Fragment -> requireNotNull(localView.activity) { "$localView not attached yet" }
                is SupportFragment -> requireNotNull(localView.context) { "$localView not attached yet" }
                else -> throw IllegalStateException("Non nullable context not supported for $view")
            }
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResult.emit(OnActivityResultEvent(requestCode, resultCode, data))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    abstract fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
            lifecycle.emit(event)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun createPresenter(view: MVPView<TView, TPresenter>) = provideInjector(view).createPresenter(view.javaClass, view.mvpTag) as TPresenter

    protected open fun provideInjector(view: MVPView<TView, TPresenter>) = (view.contextNotNull.applicationContext as MVPInjector)

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
                throw IllegalStateException("View has presenterID ($presenterId), but presenter not found!")
            }

            localPresenter = createPresenter(view)
            log("#CREATING_PRESENTER# presenterId:$presenterId ${localPresenter.javaClass.name} -> " +
                    "${view.javaClass.name}; " +
                    "ifFirstPresenterCreation: $isFirstPresenterCreation")
            presentersStorage.put(view, presenterId, localPresenter)
            localPresenter.onCreated()
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