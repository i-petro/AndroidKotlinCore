package com.androidkotlincore.sample.domain.interactors.impl

import android.content.Context
import android.content.Intent
import com.androidkotlincore.mvp.OnActivityResultEvent
import com.androidkotlincore.mvp.addons.CompositeEventListener
import com.androidkotlincore.mvp.addons.awaitFirst
import com.androidkotlincore.mvp.addons.generateRequestCode
import com.androidkotlincore.sample.data.service.GoogleSignInService
import com.androidkotlincore.sample.domain.interactors.LoginInteractor
import com.androidkotlincore.sample.domain.model.SignInModel
import javax.inject.Inject

/**
 * Created by Peter on 05.02.2018.
 */
class GoogleLoginInteractorImpl @Inject constructor(
        private val context: Context,
        private val googleSignInService: GoogleSignInService

) : LoginInteractor {

    override suspend fun login(
            activityForResultStarter: (intent: Intent, requestCode: Int) -> Unit,
            activityResultListener: CompositeEventListener<OnActivityResultEvent>): SignInModel {

        val intent = googleSignInService.generateSignInIntent()
        val requestCode = generateRequestCode()
        activityForResultStarter(intent, requestCode)
        val activityResult = activityResultListener.awaitFirst { it.requestCode == requestCode }
        val resultIntent = requireNotNull(activityResult.data)
        return googleSignInService.processSignInResult(resultIntent)
    }

    override suspend fun logout() {
        googleSignInService.logout()
    }
}