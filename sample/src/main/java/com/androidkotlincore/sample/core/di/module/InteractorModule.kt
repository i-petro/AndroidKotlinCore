package com.androidkotlincore.sample.core.di.module

import com.androidkotlincore.sample.domain.interactors.LoginInteractor
import com.androidkotlincore.sample.domain.interactors.impl.GoogleLoginInteractorImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by Peter on 05.02.2018.
 */
@Module
interface InteractorModule {
    @Singleton
    @Binds
    fun provideLoginInteractor(interactor: GoogleLoginInteractorImpl): LoginInteractor
}