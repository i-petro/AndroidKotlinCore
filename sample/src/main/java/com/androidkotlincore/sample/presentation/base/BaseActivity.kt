package com.androidkotlincore.sample.presentation.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.impl.BaseMVPActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by Peter on 04.02.2018.
 */
abstract class BaseActivity<V, P> : BaseMVPActivity<V, P>(),
        HasSupportFragmentInjector

        where V : MVPView<V, P>,
              P : MVPPresenter<P, V> {

    @Suppress("MemberVisibilityCanBePrivate")
    @Inject
    protected lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

}