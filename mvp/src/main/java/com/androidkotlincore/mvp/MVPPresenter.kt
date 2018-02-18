package com.androidkotlincore.mvp

/**
 * Created by Peter on 06.01.2017.
 */

/**
 * Represents Presenter, which can be attached to the View
 */
interface MVPPresenter<TPresenter, TView>
        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

    /**
     * Get view via coroutine
     * @return TView
     * */
    suspend fun getView(): TView

    /**
     * Executes action when View will be available on the MAIN THREAD!
     * @param action - view function
     * */
    fun postToView(action: TView.() -> Unit)

    /**
     * Attach presenter to view callback
     * @param view - view related to presenter
     * */
    fun attachView(view: TView)

    /**
     * Detach presenter to view callback
     * @param view - view related to presenter
     * */
    fun detachView(view: TView)

    /**
     * This method will be executed once after Presenter's creation
     */
    fun onCreated(isFirstPresenterCreation: Boolean)

    /**
     * This method will be executed once before Presenter's destroying
     */
    fun onDestroyed()
}