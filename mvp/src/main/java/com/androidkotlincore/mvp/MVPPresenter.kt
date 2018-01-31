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

    suspend fun getView(): TView
    fun postToView(action: TView.() -> Unit)
    fun attachView(view: TView)
    fun detachView(view: TView)

    fun onCreated(isFirstCreation: Boolean)
    fun onDestroyed()
}