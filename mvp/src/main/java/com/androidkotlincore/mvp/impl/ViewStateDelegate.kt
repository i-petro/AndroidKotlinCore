package com.androidkotlincore.mvp.impl

import android.arch.lifecycle.Lifecycle
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import java.util.*
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by Peter on 07.01.2017.
 */

/**
 * Saves and restores [MVPView] state after her re-creation
 * ViewState (applier) - a function, which will be invoked every time after [MVPView] re-creation
 * Data - data, which will be passed to the applier
 */
interface ViewStateDelegate<TPresenter, TView>
        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

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
     * saveViewState(3, "tag3",     "msg3") { showMessage(it) }
     * saveViewState(2, "tag2",     "msg2") { showMessage(it) }
     * saveViewState(3, "tag3*",    "msg3*"){ showMessage(it) }
     * saveViewState(1, "tag1",     "msg1") { showMessage(it) }
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
            presenter: MVPPresenter<TPresenter, TView>,
            id: Long,
            tag: String,
            data: TData,
            applier: TView.(data: TData) -> Unit): TData

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
            presenter: MVPPresenter<TPresenter, TView>,
            data: TData,
            applier: TView.(data: TData) -> Unit): TData

    /**
     * Restores all states for [view]
     *
     * @param view - view to restore state
     * */
    fun restoreViewState(view: TView)

    /**
     * Presenter onDestroy callback.
     * Perform any final cleanup before a presenter is destroyed
     *
     * @param presenterName - just for logs; name of presenter related to [TView]
     * */
    fun onDestroyed(presenterName: String)

    /**
     * Predicate for restoring view state
     *
     * @param previousState - [Lifecycle.Event]
     * @param currentState - [Lifecycle.Event]
     * @return true view state should be restored
     * */
    fun isRestoreViewStateRequired(previousState: Lifecycle.Event, currentState: Lifecycle.Event): Boolean
}


internal class ViewStateDelegateImpl<TPresenter, TView> : ViewStateDelegate<TPresenter, TView>
        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {
    /**
     * Provides unique id for view state by default
     * */
    private val stateIdGenerator = AtomicLong(Long.MIN_VALUE)
    /**
     * Set of view states to restore
     * */
    private val viewStates = TreeSet<ViewState<out Any?, TView>>(ViewState.comparator)

    override fun <TData> saveViewState(
            presenter: MVPPresenter<TPresenter, TView>,
            id: Long,
            tag: String,
            data: TData,
            applier: TView.(data: TData) -> Unit
    ): TData {
        return saveViewStateImpl(presenter, id, tag, data, applier)
    }

    override fun <TData> saveViewState(
            presenter: MVPPresenter<TPresenter, TView>,
            data: TData,
            applier: TView.(data: TData) -> Unit
    ): TData {
        return saveViewStateImpl(presenter, stateIdGenerator.getAndIncrement(), "?", data, applier)
    }

    private fun <TData : Any?> saveViewStateImpl(
            presenter: MVPPresenter<TPresenter, TView>,
            id: Long,
            tag: String,
            data: TData,
            applier: TView.(data: TData) -> Unit): TData {

        presenter.postToView {
            applier(this, data)

            //avoid invoking duplication
            val viewState = ViewState(id, data, applier, tag)
            if (viewStates.remove(viewState)) {
                MVPLogger.log("View state $id [$tag] [OLD] was removed from view ${this::class.java.name}")
            }
            if (viewStates.add(viewState)) {
                MVPLogger.log("View state $id [$tag] was added to view ${this::class.java.name}")
            }
        }

        return data
    }

    override fun restoreViewState(view: TView) {
        viewStates.forEach { state: ViewState<out Any?, TView> ->
            @Suppress("UNCHECKED_CAST")
            val applier = state.applier as (TView.(data: Any?) -> Unit)
            applier(view, state.data)
            MVPLogger.log("View state ${state.id} [${state.tag ?: "?"}] was restored!")
        }

        val statesCount = viewStates.count()
        if (statesCount > 0) {
            MVPLogger.log("View states was successfully restored for view ${view::class.java.name}, " +
                    "count = $statesCount")
        }
    }

    override fun onDestroyed(presenterName: String) {
        val viewStatesCount = viewStates.size
        if (viewStatesCount > 0) {
            MVPLogger.log("$presenterName: deleted view states in count: $viewStatesCount")
            viewStates.clear()
        }
    }

    override fun isRestoreViewStateRequired(previousState: Lifecycle.Event, currentState: Lifecycle.Event): Boolean {
        return previousState == Lifecycle.Event.ON_CREATE && currentState == Lifecycle.Event.ON_START
    }
}