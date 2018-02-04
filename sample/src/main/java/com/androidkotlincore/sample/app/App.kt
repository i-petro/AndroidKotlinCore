package com.androidkotlincore.sample.app

import android.app.Activity
import android.app.Application
import android.app.Service
import com.androidkotlincore.mvp.MVPInjector
import com.androidkotlincore.mvp.impl.MVP_LOGGING_ENABLED
import com.androidkotlincore.sample.core.di.DI
import com.androidkotlincore.sample.core.di.DaggerAppComponent
import com.androidkotlincore.sample.core.di.MVPInjectorDelegate
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

/**
 * Created by Peter on 04.02.2018.
 */
@Suppress("ProtectedInFinal")
class App : Application(), HasActivityInjector, HasServiceInjector, MVPInjector by MVPInjectorDelegate() {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var dispatchingAndroidInjectorServices: DispatchingAndroidInjector<Service>

    override fun onCreate() {
        super.onCreate()

        val component = DaggerAppComponent
                .builder()
                .context(this)
                .build()
        component.inject(this)
        DI.init(component)

        MVP_LOGGING_ENABLED = true
    }

    override fun onTerminate() {
        DI.destroy()
        super.onTerminate()
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun serviceInjector(): AndroidInjector<Service> = dispatchingAndroidInjectorServices
}