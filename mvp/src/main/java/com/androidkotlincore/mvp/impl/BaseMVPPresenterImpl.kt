package com.androidkotlincore.mvp.impl

import android.arch.lifecycle.Lifecycle
import android.os.Handler
import android.os.Looper
import com.androidkotlincore.mvp.*
import com.androidkotlincore.mvp.addons.StateExecutor
import com.androidkotlincore.mvp.impl.MVPLogger.log
import com.androidkotlincore.mvp.impl.permissions.OnRequestPermissionsResultEvent
import com.androidkotlincore.mvp.impl.permissions.PermissionsManager
import com.androidkotlincore.mvp.impl.permissions.PermissionsManagerDelegate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

/**
 * Created by Peter on 06.01.2017.
 */
abstract class BaseMVPPresenterImpl<TPresenter, TView>(
        private val subscriptions: SubscriptionContainer,
        private val viewStateDelegate: ViewStateDelegate<TPresenter, TView>) :
        MVPPresenter<TPresenter, TView>,
        SubscriptionContainer by subscriptions

        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

    constructor(): this(SubscriptionsContainerDelegate(), ViewStateDelegateImpl())

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var mvpView = StateExecutor<TView?, TView>(initState = null) { it != null }
    //view lifecycle
    private val viewLifecycleEventsDelegate = BehaviorSubject.create<Lifecycle.Event>()
    private var viewLifecycleDisposable: Disposable? = null
    val viewLifecycle get() = viewLifecycleEventsDelegate.hide()!!
    //on activity result
    private val onActivityResultEventsDelegate = BehaviorSubject.create<OnActivityResultEvent>()
    private var onActivityResultDisposable: Disposable? = null
    val onActivityResult get() = onActivityResultEventsDelegate.hide()!!
    //on request permission result
    private val onRequestPermissionsResultDelegate = BehaviorSubject.create<OnRequestPermissionsResultEvent>()
    private var onRequestPermissionsResultDisposable: Disposable? = null
    val permissions: PermissionsManager by lazy {
        PermissionsManagerDelegate(onRequestPermissionsResultDelegate.hide(), view = { getView() })
    }

    //////////////////////////////////////// VIEW STATES ///////////////////////////////////////////
    /**
     * Invokes "applier" function on View immediately when View will be appear and
     * every time when View will be recreated.
     * @param data - data to be transferred to View
     * @param applier - function, which will be applied with data on View.
     * Note! Do not use other parameters except "data"!
     * Note! In the applier function you must call ONLY View's functions
     * Note! Use different "saveViewState" invocations for each operation.
     * Note! This function must be invoked in the presenter, not in the "postToView{}"!
     * Example:
     * saveViewState("hello!") { message = it }
     */
    fun <TData : Any?> saveViewState(
            data: TData,
            applier: TView.(data: TData) -> Unit): TData =
            viewStateDelegate.saveViewState(this, data, applier)

    /**
     * Invokes "applier" function on View immediately when View will be appear and
     * every time when View will be recreated.
     * @param id - unique operation identifier. Always will be saved only LAST operation with the same id!
     *             After restoring View state, all operations will be invoked with "id" order.
     * @param tag - any String, which will be printed to logs
     * @param data - data to be transferred to View
     * @param applier - function, which will be applied with data on View.
     * Note! Use different id's for different operations.
     *       Use same id's for when you want to save ONLY LAST operation.
     * Note! Do not use other parameters except "data"!
     * Note! In the applier function you must call ONLY View's functions
     * Note! Use different "saveViewState" invocations for each operation.
     * Note! This function must be invoked in the presenter, not in the "postToView{}"!
     * Example:
     * saveViewState(3, "tag3", "msg3") { showMessage(it) }
     * saveViewState(2, "tag2", "msg2") { showMessage(it) }
     * saveViewState(3, "tag3*", "msg3*") { showMessage(it) }
     * saveViewState(1, "tag1", "msg1") { showMessage(it) }
     * Result after invoke:
     * "msg3"
     * "msg2"
     * "msg3*"
     * "msg1"
     * Result after restoring view state:
     * "msg1"
     * "msg2"
     * "msg3*"
     */
    fun <TData : Any?> saveViewState(
            id: Long,
            tag: String,
            data: TData,
            applier: TView.(data: TData) -> Unit): TData {
        return viewStateDelegate.saveViewState(this, id, tag, data, applier)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Executes action when View will be available on the MAIN THREAD!
     */
    override fun postToView(action: TView.() -> Unit) = run { mainThreadHandler.post { mvpView.invoke(action)}; Unit }

    override suspend fun getView(): TView {
        return mvpView.value ?: suspendCancellableCoroutine { continuation ->
            mvpView.invoke { continuation.resume(it) }
            addJob(continuation)
        }
    }

    override fun onCreated(isFirstCreation: Boolean) {
        //empty implementation
    }

    final override fun attachView(view: TView) {
        //attach lifecycle events delegate
        viewLifecycleDisposable?.dispose()
        viewLifecycleDisposable = view.lifecycle
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewLifecycleEventsDelegate::onNext)
                .bind(this)
        //attach to activity result events
        onActivityResultDisposable?.dispose()
        onActivityResultDisposable = view.onActivityResult
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onActivityResultEventsDelegate::onNext)
                .bind(this)
        //attach to permission result events
        onRequestPermissionsResultDisposable?.dispose()
        onRequestPermissionsResultDisposable = view.onRequestPermissionResult
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onRequestPermissionsResultDelegate::onNext)
                .bind(this)

        mvpView.value = view

        viewStateDelegate.restoreViewState(view)

        onViewAttached(view)
    }

    open fun onViewAttached(view: TView) {
        //do something
    }

    final override fun detachView(view: TView) {
        //detach lifecycle events delegate
        viewLifecycleDisposable?.dispose()
        //detach from OnActivityResult events
        onActivityResultDisposable?.dispose()
        //detach from OnRequestPermissionsResult events
        onRequestPermissionsResultDisposable?.dispose()
        //clear view
        mvpView.value = null
        onViewDetached(view)
    }

    /**
     * Last chance to get view
     */
    open fun onViewDetached(view: TView) {
        //do something
    }

    override fun onDestroyed() {
        val presenterName = this::class.java.name

        val unInvokedActionsCount = mvpView.actionsCount()
        if (unInvokedActionsCount != 0) {
            log("$presenterName: some [$unInvokedActionsCount] postToView() actions have been lost when onDestroyed() was called")
            mvpView.clear()
        }

        viewStateDelegate.onDestroyed(presenterName)

        mainThreadHandler.removeCallbacksAndMessages(null)
        subscriptions.clearSubscriptionsAndJobs()
    }
}