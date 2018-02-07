package com.androidkotlincore.sample.core.di.module

import com.androidkotlincore.sample.core.di.module.activity.ContainerActivityModule
import com.androidkotlincore.sample.core.di.module.activity.SplashActivityModule
import com.androidkotlincore.sample.core.di.scope.ActivityScope
import com.androidkotlincore.sample.presentation.screen.container.FragmentContainerActivity
import com.androidkotlincore.sample.presentation.screen.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Created by Peter on 04.02.2018.
 */
@Module(includes = arrayOf(AndroidSupportInjectionModule::class))
interface AndroidModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(ContainerActivityModule::class))
    fun fragmentContainerActivityInjector(): FragmentContainerActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(SplashActivityModule::class))
    fun splashActivityInjector(): SplashActivity
}