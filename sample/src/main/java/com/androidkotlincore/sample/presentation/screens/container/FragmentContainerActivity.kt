package com.androidkotlincore.sample.presentation.screens.container

import com.androidkotlincore.sample.R
import com.androidkotlincore.sample.presentation.base.BaseActivity

/**
 * Created by Peter on 04.02.2018.
 */
class FragmentContainerActivity : BaseActivity<FragmentContainerView, FragmentContainerPresenter>(), FragmentContainerView {
    override val layoutId: Int = R.layout.activity_fragment_container
}