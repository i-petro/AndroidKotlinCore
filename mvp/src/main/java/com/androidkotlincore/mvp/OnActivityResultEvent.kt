package com.androidkotlincore.mvp

import android.content.Intent

/**
 * Created by Peter on 01.12.2017.
 */
/**
 * Represents an OnActivityResult event
 */
data class OnActivityResultEvent(val requestCode: Int, val resultCode: Int, val data: Intent?)