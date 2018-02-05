package com.androidkotlincore.sample.core.di.module

import com.androidkotlincore.sample.data.service.GoogleSignInService
import com.androidkotlincore.sample.data.service.impl.GoogleSignInServiceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by Peter on 05.02.2018.
 */
@Module
interface GoogleSignInModule {
    @Singleton
    @Binds
    fun provideGoogleSignInService(service: GoogleSignInServiceImpl): GoogleSignInService
}