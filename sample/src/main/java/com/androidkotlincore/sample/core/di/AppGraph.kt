package com.androidkotlincore.sample.core.di

import android.content.Context
import com.androidkotlincore.sample.presentation.screen.login.LoginPresenterImpl

/**
 * Created by Peter on 04.02.2018.
 */
interface AppGraph {
    val context: Context
    fun inject(app: LoginPresenterImpl)

    //inject...
}