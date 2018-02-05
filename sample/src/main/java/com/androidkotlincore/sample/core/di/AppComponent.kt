package com.androidkotlincore.sample.core.di

import android.content.Context
import com.androidkotlincore.sample.app.App
import com.androidkotlincore.sample.core.di.module.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Peter on 04.02.2018.
 */
@Singleton
@Component(modules = [
    AppModule::class,
    AndroidModule::class,
    ScreenProviderModule::class,
    InteractorModule::class,
    GoogleSignInModule::class
])
interface AppComponent : AppGraph {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}