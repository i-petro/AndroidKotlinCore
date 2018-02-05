package com.androidkotlincore.sample.presentation.screen.login

import android.content.Context
import com.androidkotlincore.sample.presentation.screen.container.FragmentContainerScreenManager
import javax.inject.Inject

/**
 * Created by Peter on 05.02.2018.
 */
class LoginScreenManagerImpl @Inject constructor() : LoginScreenManager {
    @Inject protected lateinit var container: FragmentContainerScreenManager

    override fun start(context: Context) {
        container.start(context, LoginFragment::class.java)
    }
}