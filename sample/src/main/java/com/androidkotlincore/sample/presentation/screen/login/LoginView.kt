package com.androidkotlincore.sample.presentation.screen.login

import android.content.Intent
import com.androidkotlincore.sample.presentation.base.BaseView

/**
 * Created by Peter on 04.02.2018.
 */
interface LoginView : BaseView<LoginView, LoginPresenter> {
    fun startActivityForResult(intent: Intent, requestCode: Int)
    fun showMessage(message: String)
}