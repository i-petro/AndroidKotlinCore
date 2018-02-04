package com.androidkotlincore.sample.core.di

import android.content.Context

/**
 * Created by Peter on 04.02.2018.
 */

/**
 * Dependency injector class
 * Before first usage call [init] method
 * After last usage call [destroy] method
 */
object DI {
    private var appComponent: AppComponent? = null

    val context: Context
        get() = appComponent?.context
                ?: throw DependencyProviderException("Call init() method before!")
    val component: AppComponent get() = appComponent ?: throw DependencyProviderException("Call init() method before!")

    fun init(component: AppComponent) {
        this.appComponent = component
    }

    fun destroy() {
        appComponent = null
    }

    class DependencyProviderException(s: String) : IllegalStateException(s)
}