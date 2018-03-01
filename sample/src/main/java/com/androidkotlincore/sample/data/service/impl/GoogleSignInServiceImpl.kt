package com.androidkotlincore.sample.data.service.impl

import android.content.Context
import android.content.Intent
import com.androidkotlincore.entityconverter.ConvertersContext
import com.androidkotlincore.entityconverter.convert
import com.androidkotlincore.sample.R
import com.androidkotlincore.sample.data.service.GoogleSignInService
import com.androidkotlincore.sample.domain.model.SignInModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import javax.inject.Inject

/**
 * Created by Peter on 05.02.2018.
 */
class GoogleSignInServiceImpl @Inject constructor(ctx: Context, convertersContext: ConvertersContext) : GoogleSignInService {
    private val context: Context = ctx
    private val converter: ConvertersContext = convertersContext
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.firebase_request_id_token))
                .requestEmail()
                .requestProfile()
                .build()
        GoogleSignIn.getClient(context, gso)
    }

    override fun generateSignInIntent(): Intent = googleSignInClient.signInIntent

    override suspend fun processSignInResult(result: Intent): SignInModel {
        val googleSignInAccount: GoogleSignInAccount =
                suspendCancellableCoroutine { continuation ->
                    Auth.GoogleSignInApi.getSignInResultFromIntent(result)
                    GoogleSignIn.getSignedInAccountFromIntent(result).addOnCompleteListener {
                        when {
                            it.isSuccessful -> continuation.resume(it.result)
                            //TODO: ApiException: 12502 - SignIn in progress after screen rotating
                            else -> continuation.resumeWithException(it.exception
                                    ?: IllegalStateException("getSignedInAccountFromIntent() unknown error"))
                        }
                    }
                }

        val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)

        val authResult = suspendCancellableCoroutine<AuthResult> { continuation ->
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                when {
                    it.isSuccessful -> continuation.resume(it.result)
                    else -> continuation.resumeWithException(it.exception
                            ?: IllegalStateException("signInWithCredential() unknown error"))
                }
            }
        }

        return converter.convert(authResult)
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
        suspendCancellableCoroutine<Unit> { continuation ->
            googleSignInClient.signOut().addOnCompleteListener {
                when {
                    it.isSuccessful -> continuation.resume(Unit)
                    else -> continuation.resumeWithException(it.exception
                            ?: IllegalStateException("signOut() unknown error"))
                }
            }
        }
    }
}