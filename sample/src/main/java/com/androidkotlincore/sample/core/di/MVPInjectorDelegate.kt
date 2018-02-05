package com.androidkotlincore.sample.core.di

import android.os.Bundle
import com.androidkotlincore.mvp.MVPInjector
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.sample.presentation.screen.container.FragmentContainerActivity
import com.androidkotlincore.sample.presentation.screen.container.FragmentContainerPresenterImpl
import com.androidkotlincore.sample.presentation.screen.login.LoginFragment
import com.androidkotlincore.sample.presentation.screen.login.LoginPresenterImpl
import com.androidkotlincore.sample.presentation.screen.splash.SplashActivity
import com.androidkotlincore.sample.presentation.screen.splash.SplashPresenterImpl

/**
 * Created by Peter on 04.02.2018.
 */
class MVPInjectorDelegate : MVPInjector {
    override fun createPresenter(viewJavaClass: Class<in MVPView<*, *>>, mvpTag: Bundle): MVPPresenter<*, *> =
            when (viewJavaClass) {
                SplashActivity::class.java -> SplashPresenterImpl()
                FragmentContainerActivity::class.java -> FragmentContainerPresenterImpl()
                LoginFragment::class.java -> LoginPresenterImpl()
                else -> throwPresenterNotFound(viewJavaClass, mvpTag)
            }

    private fun throwPresenterNotFound(viewJavaClass: Class<in MVPView<*, *>>, mvpTag: Bundle): Nothing {
        throw IllegalArgumentException("Illegal argument viewJavaClass $viewJavaClass, mvpTag: $mvpTag")
    }
}