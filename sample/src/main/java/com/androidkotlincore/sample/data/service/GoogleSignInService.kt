package com.androidkotlincore.sample.data.service

import android.content.Intent
import com.androidkotlincore.sample.domain.model.SignInModel

/**
 * Created by Peter on 05.02.2018.
 */
interface GoogleSignInService {
    fun generateSignInIntent(): Intent
    suspend fun processResult(result: Intent): SignInModel
}