package com.androidkotlincore.sample.presentation.screen.login

import com.androidkotlincore.sample.presentation.base.BasePresenter

/**
 * Created by Peter on 04.02.2018.
 */
interface LoginPresenter : BasePresenter<LoginPresenter, LoginView> {
    fun signInWithGoogle()
    fun logout()
}