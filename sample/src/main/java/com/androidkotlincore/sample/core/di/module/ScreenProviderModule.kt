package com.androidkotlincore.sample.core.di.module

import com.androidkotlincore.sample.presentation.screen.container.FragmentContainerScreenManager
import com.androidkotlincore.sample.presentation.screen.container.FragmentContainerScreenManagerImpl
import com.androidkotlincore.sample.presentation.screen.login.LoginScreenManager
import com.androidkotlincore.sample.presentation.screen.login.LoginScreenManagerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by Peter on 05.02.2018.
 */
@Module
interface ScreenProviderModule {

    @Singleton
    @Binds
    fun provideLoginActivityManager(manager: LoginScreenManagerImpl): LoginScreenManager

    @Singleton
    @Binds
    fun provideScreenContainerManager(manager: FragmentContainerScreenManagerImpl): FragmentContainerScreenManager
}