package com.androidkotlincore.mvp

import android.os.Bundle

/**
 * Created by Peter on 07.01.2017.
 */

/**
 * Represents an Presenter's injector
 * Note!!! Your [android.app.Application] class must implements this interface.
 * The interface allows the [MVPView]'s layer and the [MVPPresenter]'s layer to be made independent of each other.
 * You can use the same [MVPPresenter] for the different [MVPView]'s and use multiple [MVPPresenter]'s for the single [MVPView].
 * To determine which [MVPPresenter] you must create, use "viewJavaClass" and "mvpTag" params.
 * To specify "mvpTag", please override [com.androidkotlincore.mvp.MVPView.mvpTag] property.
 *
 */
interface MVPInjector {
    /**
     * This method must returns new [MVPPresenter] for [MVPView]
     * @param viewJavaClass - key to select what presenter should be used
     * @param mvpTag - bundle with additional keys to select different presenters for the same view class
     */
    fun createPresenter(viewJavaClass: Class<in MVPView<*, *>>, mvpTag: Bundle): MVPPresenter<*, *>
}