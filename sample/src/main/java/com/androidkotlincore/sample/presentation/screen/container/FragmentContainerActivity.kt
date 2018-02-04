package com.androidkotlincore.sample.presentation.screen.container

import android.os.Bundle
import com.androidkotlincore.sample.R
import com.androidkotlincore.sample.presentation.base.BaseActivity
import com.androidkotlincore.sample.presentation.screen.login.LoginFragment

/**
 * Created by Peter on 04.02.2018.
 */
class FragmentContainerActivity : BaseActivity<FragmentContainerView, FragmentContainerPresenter>(), FragmentContainerView {
    override val layoutId: Int = R.layout.activity_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, LoginFragment.newInstance())
                    .commit()
        }
    }
}