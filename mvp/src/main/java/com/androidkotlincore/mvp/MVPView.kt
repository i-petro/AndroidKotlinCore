package com.androidkotlincore.mvp

import android.content.Context
import android.os.Bundle

/**
 * Created by Peter on 06.01.2017.
 */

/**
 * Represents View, which have lifecycle
 * All Activities and Fragments must implement this interface
 * @see [com.androidkotlincore.mvp.impl.BaseMVPActivity]
 * @see [com.androidkotlincore.mvp.impl.BaseMVPFragment]
 * @see [com.androidkotlincore.mvp.impl.BaseMVPDialog]
 */
interface MVPView<TView, TPresenter> : LifecycleOwner
        where TView : MVPView<TView, TPresenter>,
              TPresenter : MVPPresenter<TPresenter, TView> {

    val presenter: TPresenter
    val contextNotNull: Context
    val mvpTag: Bundle get() = Bundle()
}