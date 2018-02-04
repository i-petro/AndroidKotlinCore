package com.androidkotlincore.sample.core.di

import android.content.Context
import com.androidkotlincore.sample.app.App
import com.androidkotlincore.sample.core.di.module.AndroidModule
import com.androidkotlincore.sample.core.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Peter on 04.02.2018.
 */
@Singleton
@Component(modules = [
    AppModule::class,
    AndroidModule::class
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