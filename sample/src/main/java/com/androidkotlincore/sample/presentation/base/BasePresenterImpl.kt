package com.androidkotlincore.sample.presentation.base

import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.impl.BaseMVPPresenterImpl

/**
 * Created by Peter on 04.02.2018.
 */
abstract class BasePresenterImpl<P, V> : BaseMVPPresenterImpl<P, V>()

        where V : MVPView<V, P>,
              P : MVPPresenter<P, V> {
}