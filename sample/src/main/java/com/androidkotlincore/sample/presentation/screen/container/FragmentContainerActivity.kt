package com.androidkotlincore.sample.presentation.screen.container

import android.os.Bundle
import android.support.v4.app.Fragment
import com.androidkotlincore.sample.R
import com.androidkotlincore.sample.presentation.base.BaseActivity

/**
 * Created by Peter on 04.02.2018.
 */
class FragmentContainerActivity : BaseActivity<FragmentContainerView, FragmentContainerPresenter>(), FragmentContainerView {
    override val layoutId: Int = R.layout.activity_fragment_container

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragmentClass = intent.getSerializableExtra(EXTRA_FRAGMENT_CLASS) as Class<Fragment>
            val arguments = intent.getBundleExtra(EXTRA_FRAGMENT_ARGUMENTS)

            val fragment = fragmentClass.newInstance()
            fragment.arguments = arguments

            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()
        }
    }

    companion object {
        internal const val EXTRA_FRAGMENT_CLASS = "EXTRA_FRAGMENT_CLASS"
        internal const val EXTRA_FRAGMENT_ARGUMENTS = "EXTRA_FRAGMENT_ARGUMENTS"
    }
}