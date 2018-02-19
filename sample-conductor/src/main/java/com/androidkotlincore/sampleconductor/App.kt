package com.androidkotlincore.sampleconductor

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.androidkotlincore.mvp.MVPInjector
import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import com.androidkotlincore.mvp.impl.MVP_LOGGING_ENABLED
import com.androidkotlincore.sampleconductor.demo.DemoPresenterImpl

/**
 * Created by Peter on 19.02.2018.
 */
class App : Application(), MVPInjector {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        MVP_LOGGING_ENABLED = true
    }

    override fun createPresenter(viewJavaClass: Class<in MVPView<*, *>>, mvpTag: Bundle): MVPPresenter<*, *> {
        return DemoPresenterImpl()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var INSTANCE: Context
    }
}