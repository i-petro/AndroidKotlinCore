package com.androidkotlincore.mvp.impl

import android.util.Log
import com.androidkotlincore.mvp.BuildConfig

/**
 * Created by Peter on 23.11.17.
 */
internal object MVPLogger {
    private const val TAG = "MVPLogger"

    fun log(message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, message)
        }
    }
}