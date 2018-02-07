package com.androidkotlincore.sample.domain.interactors.impl

import android.content.Context
import android.content.Intent
import com.androidkotlincore.mvp.OnActivityResultEvent
import com.androidkotlincore.mvp.addons.CompositeEventListener
import com.androidkotlincore.mvp.addons.awaitFirst
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
            startActivityForResult: (intent: Intent, requestCode: Int) -> Unit,
            getActivityResult: CompositeEventListener<OnActivityResultEvent>): SignInModel {

        val intent = googleSignInService.generateSignInIntent()
        startActivityForResult(intent, REQUEST_CODE_GOOGLE_SIGN_IN)
        val activityResult = getActivityResult.awaitFirst { it.requestCode == REQUEST_CODE_GOOGLE_SIGN_IN }
        val resultIntent = requireNotNull(activityResult.data)
        val signInResult = googleSignInService.processResult(resultIntent)
        return signInResult
    }

    companion object {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 17
    }
}