package com.androidkotlincore.sample.presentation.base

import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.impl.BaseMVPFragment

/**
 * Created by Peter on 04.02.2018.
 */
abstract class BaseFragment<V, P> : BaseMVPFragment<V, P>()

        where V : MVPView<V, P>,
              P : MVPPresenter<P, V> {
}