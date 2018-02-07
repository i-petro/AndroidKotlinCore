package com.androidkotlincore.sample.data.service.impl

import android.content.Context
import android.content.Intent
import com.androidkotlincore.sample.R
import com.androidkotlincore.sample.data.model.impl.SignInResultImpl
import com.androidkotlincore.sample.data.service.GoogleSignInService
import com.androidkotlincore.sample.domain.model.SignInModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject
import kotlin.coroutines.experimental.suspendCoroutine

/**
 * Created by Peter on 05.02.2018.
 */
class GoogleSignInServiceImpl @Inject constructor(val context: Context) : GoogleSignInService {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.firebase_request_id_token))
                .requestEmail()
                .build()
        GoogleSignIn.getClient(context, gso)
    }

    override fun generateSignInIntent(): Intent = googleSignInClient.signInIntent

    override suspend fun processResult(result: Intent): SignInModel {
        val googleSignInAccount = GoogleSignIn.getSignedInAccountFromIntent(result).result

        val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)

        val authResult = suspendCoroutine<AuthResult> { continuation ->
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener({
                when {
                    it.isSuccessful -> continuation.resume(it.result)
                    else -> continuation.resumeWithException(it.exception
                            ?: IllegalStateException("signInWithCredential() unknown error"))
                }
            })
        }

        return SignInResultImpl(
                isNewUser = authResult.additionalUserInfo.isNewUser,
                userName = authResult.additionalUserInfo.username,
                providerId = authResult.user.providerId,
                email = authResult.user.email,
                displayName = authResult.user.displayName,
                uid = authResult.user.uid,
                phoneNumber = authResult.user.phoneNumber
        )
    }
}