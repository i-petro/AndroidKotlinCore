package com.androidkotlincore.mvp.impl

import android.arch.lifecycle.Lifecycle
import android.os.Handler
import android.os.Looper
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.OnActivityResultEvent
import com.androidkotlincore.mvp.addons.CompositeEventListener
import com.androidkotlincore.mvp.addons.EmitableCompositeEventListener
import com.androidkotlincore.mvp.addons.StateExecutor
import com.androidkotlincore.mvp.addons.impl.SimpleCompositeEventListener
import com.androidkotlincore.mvp.impl.MVPLogger.log
import com.androidkotlincore.mvp.impl.permissions.OnRequestPermissionsResultEvent
import com.androidkotlincore.mvp.impl.permissions.PermissionsManager
import com.androidkotlincore.mvp.impl.permissions.PermissionsManagerDelegate
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

/**
 * Created by Peter on 06.01.2017.
 */
/**
 * Base MVP presenter. Contains core features.
 * @param subscriptions - container to add subscriptions in it and clear it when presenter should be destroyed
 * @param viewStateDelegate - delegate to save some action which should de done on view after view is recreated
 * */
abstract class BaseMVPPresenterImpl<TPresenter, TView>(
        private val subscriptions: SubscriptionContainer,
        private val viewStateDelegate: ViewStateDelegate<TPresenter, TView>) :
        MVPPresenter<TPresenter, TView>,
        SubscriptionContainer by subscriptions

        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

    constructor() : this(SubscriptionsContainerDelegate(), ViewStateDelegateImpl())

    /**
     * Handler to execute action on main thread
     * */
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    /**
     * Allows to delay actions on view until view will be available
     * */
    private var mvpView = StateExecutor<TView?, TView>(initState = null) { it != null }
    /**
     * Emits [Lifecycle.Event]
     * */
    private val viewLifecycleEmitter: EmitableCompositeEventListener<Lifecycle.Event> = SimpleCompositeEventListener()
    /**
     * Use it to subscribe on [Lifecycle.Event]
     * */
    val viewLifecycle: CompositeEventListener<Lifecycle.Event> = viewLifecycleEmitter
    /**
     * Is used to delegate events from view to presenter
     * */
    private var lifecycleListener: ((Lifecycle.Event) -> Unit)? = null

    /**
     * Emits [OnActivityResultEvent]
     * */
    private val onActivityResultEmitter: EmitableCompositeEventListener<OnActivityResultEvent> = SimpleCompositeEventListener()
    /**
     * Use it to subscribe on [OnActivityResultEvent]
     * */
    val onActivityResult: CompositeEventListener<OnActivityResultEvent> = onActivityResultEmitter
    /**
     * Is used to delegate events from view to presenter
     * */
    private var onActivityResultListener: ((OnActivityResultEvent) -> Unit)? = null

    /**
     * Emits [OnRequestPermissionsResultEvent]
     * */
    private val onRequestPermissionsResultDelegateEmitter: EmitableCompositeEventListener<OnRequestPermissionsResultEvent> = SimpleCompositeEventListener()
    /**
     * Use it to subscribe on [OnRequestPermissionsResultEvent]
     * */
    private val onRequestPermissionsResultDelegate: CompositeEventListener<OnRequestPermissionsResultEvent> = onRequestPermissionsResultDelegateEmitter
    /**
     * Is used to delegate events from view to presenter
     * */
    private var onRequestPermissionsListener: ((OnRequestPermissionsResultEvent) -> Unit)? = null
    /**
     * Lazy initialization of [PermissionsManagerDelegate]
     * */
    val permissions: PermissionsManager by lazy {
        PermissionsManagerDelegate(onRequestPermissionsResultDelegate, view = { getView() })
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
    override fun postToView(action: TView.() -> Unit) = run { mainThreadHandler.post { mvpView.invoke(action) }; Unit }

    /**
     * @see [MVPPresenter]
     * */
    override suspend fun getView(): TView {
        return mvpView.value ?: suspendCancellableCoroutine { continuation ->
            mvpView.invoke { continuation.resume(it) }
            addJob(continuation)
        }
    }

    /**
     * This method will be executed once after Presenter creation
     */
    override fun onCreated() {
        //empty implementation
    }

    /**
     * @see [MVPPresenter.attachView]
     * */
    final override fun attachView(view: TView) {
        /**
         * 1. subscribe presenter to view lifecycle events and restore view state when it is required
         */
        var previousEvent: Lifecycle.Event? = null
        lifecycleListener = view.lifecycle.subscribe { currentEvent ->
            val localPreviousEvent = previousEvent
            if (localPreviousEvent != null && viewStateDelegate.isRestoreViewStateRequired(localPreviousEvent, currentEvent)) {
                viewStateDelegate.restoreViewState(view)
            }
            viewLifecycleEmitter.emit(currentEvent)
            previousEvent = currentEvent
        }

        /**
         * 2. subscribe presenter to OnActivityResult events
         */
        onActivityResultListener = view.onActivityResult.subscribe {
            onActivityResultEmitter.emit(it)
        }

        /**
         * 3. subscribe presenter to onRequestPermissionsResult events
         */
        onRequestPermissionsListener = view.onRequestPermissionResult.subscribe {
            onRequestPermissionsResultDelegateEmitter.emit(it)
        }

        /**
         * 4. finally attach view to the presenter and execute [postToView] queue
         */
        mvpView.value = view

        /**
         * 5. notify child presenters
         */
        onViewAttached(view)
    }

    /**
     * This function will be executed after each attachment of the View
     */
    open fun onViewAttached(view: TView) {
        //do something
    }

    /**
     * @see [MVPPresenter.detachView]
     * */
    final override fun detachView(view: TView) {
        /**
         * 1. unSubscribe presenter from lifecycle events delegate
         */
        lifecycleListener?.let { view.lifecycle.unSubscribe(it) }
        /**
         * 2. unSubscribe presenter from OnActivityResult events
         */
        onActivityResultListener?.let { view.onActivityResult.unSubscribe(it) }
        /**
         * 3. unSubscribe presenter from OnRequestPermissionsResult events
         */
        onRequestPermissionsListener?.let { view.onRequestPermissionResult.unSubscribe(it) }
        /**
         * 4. clear view
         */
        mvpView.value = null
        /**
         * 5. notify child presenters
         */
        onViewDetached(view)
    }

    /**
     * This function will be executed after each detachment of the View
     */
    open fun onViewDetached(view: TView) {
        //do something
    }

    /**
     * @see [MVPPresenter.onDestroyed]
     * */
    override fun onDestroyed() {
        val presenterName = this::class.java.name

        val unInvokedActionsCount = mvpView.actionsCount()
        if (unInvokedActionsCount != 0) {
            log("$presenterName: some [$unInvokedActionsCount] postToView() actions have been lost when onDestroyed() was called")
            mvpView.clear()
        }

        viewStateDelegate.onDestroyed(presenterName)

        mainThreadHandler.removeCallbacksAndMessages(null)
        subscriptions.cancelAllSubscriptions()
    }
}