package com.androidkotlincore.sample.presentation.screen.splash

import com.androidkotlincore.sample.R
import com.androidkotlincore.sample.presentation.base.BaseActivity
import com.androidkotlincore.sample.presentation.screen.login.LoginScreenManager
import javax.inject.Inject

/**
 * Created by Peter on 05.02.2018.
 */
class SplashActivity : BaseActivity<SplashView, SplashPresenter>(), SplashView {
    override val layoutId: Int = R.layout.activity_splash

    @Suppress("ProtectedInFinal")
    @Inject
    protected lateinit var loginScreenManager: LoginScreenManager

    override fun openLoginScreen() = loginScreenManager.start(this)
}