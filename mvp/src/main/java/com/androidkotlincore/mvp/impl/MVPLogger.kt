package com.androidkotlincore.mvp.impl

import android.util.Log
import com.androidkotlincore.mvp.BuildConfig

/**
 * This flag can be used to enable logs in debug mode.
 * Logs are disabled by default
 * */
var MVP_LOGGING_ENABLED = false

/**
 * Created by Peter on 23.11.17.
 */
/**
 * Logger util
 * */
internal object MVPLogger {
    private const val TAG = "MVPLogger"

    fun log(message: String) {
        if (BuildConfig.DEBUG || MVP_LOGGING_ENABLED) {
            Log.i(TAG, message)
        }
    }
}