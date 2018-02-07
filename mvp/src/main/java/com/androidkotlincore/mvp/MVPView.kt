package com.androidkotlincore.mvp

import android.content.Context
import android.os.Bundle

/**
 * Created by Peter on 06.01.2017.
 */

/**
 * Represents View, which have a lifecycle
 * All Activities and Fragments must implement this interface
 * @see [com.androidkotlincore.mvp.impl.BaseMVPActivity]
 * @see [com.androidkotlincore.mvp.impl.BaseMVPFragment]
 * @see [com.androidkotlincore.mvp.impl.BaseMVPDialog]
 * @property presenter - presenter for this view
 * @property contextNotNull - provides non null context
 * @property mvpTag - view bundle. Contains passed to view arguments. Also can contain key
 * for [com.androidkotlincore.mvp.MVPInjector] to select what presenter should be provided for this view.
 * It is useful when you want to reuse view with different presenters
 */
interface MVPView<TView, TPresenter> : LifecycleOwner
        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

    val presenter: TPresenter
    val contextNotNull: Context
    val mvpTag: Bundle get() = Bundle()
}