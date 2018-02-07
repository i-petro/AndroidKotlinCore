package com.androidkotlincore.sample.core.di.module

import com.androidkotlincore.sample.app.AppInfoProvider
import com.androidkotlincore.sample.app.AppInfoProviderImpl
import com.androidkotlincore.sample.app.BuildConfigInfoProvider
import com.androidkotlincore.sample.app.BuildConfigInfoProviderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by Peter on 04.02.2018.
 */
@Module
interface AppModule {
    @Singleton
    @Binds
    fun provideBuildConfig(config: BuildConfigInfoProviderImpl): BuildConfigInfoProvider

    @Singleton
    @Binds
    fun provideAppInfo(appInfo: AppInfoProviderImpl): AppInfoProvider
}