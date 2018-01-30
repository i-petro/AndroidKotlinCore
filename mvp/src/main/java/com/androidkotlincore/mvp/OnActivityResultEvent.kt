package com.androidkotlincore.mvp

import android.content.Intent

/**
 * Created by Peter on 01.12.2017.
 */
data class OnActivityResultEvent(val requestCode: Int, val resultCode: Int, val data: Intent?)