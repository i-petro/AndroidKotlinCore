package com.androidkotlincore.sample.presentation.base

import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView

/**
 * Created by Peter on 04.02.2018.
 */
interface BasePresenter<P, V> : MVPPresenter<P, V>

        where V : MVPView<V, P>,
              P : MVPPresenter<P, V> {
}