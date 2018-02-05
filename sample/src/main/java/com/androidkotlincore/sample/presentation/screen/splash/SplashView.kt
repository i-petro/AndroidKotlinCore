package com.androidkotlincore.sample.presentation.screen.splash

import com.androidkotlincore.sample.presentation.base.BaseView

/**
 * Created by Peter on 05.02.2018.
 */
interface SplashView : BaseView<SplashView, SplashPresenter> {
    fun openLoginScreen()
    fun finish()
}