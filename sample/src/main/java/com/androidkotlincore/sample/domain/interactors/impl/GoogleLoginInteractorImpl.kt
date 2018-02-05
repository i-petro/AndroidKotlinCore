package com.androidkotlincore.sample.domain.interactors.impl

import android.content.Context
import javax.inject.Inject

/**
 * Created by Peter on 05.02.2018.
 */
class GoogleLoginInteractorImpl @Inject constructor(
        val context: Context) {


    companion object {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 17
    }
}