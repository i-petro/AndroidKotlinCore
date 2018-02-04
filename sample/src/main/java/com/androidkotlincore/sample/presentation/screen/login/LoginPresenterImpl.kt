package com.androidkotlincore.sample.presentation.screen.login

import android.content.Context
import com.androidkotlincore.mvp.addons.awaitFirst
import com.androidkotlincore.mvp.impl.launchUI
import com.androidkotlincore.sample.core.di.DI
import com.androidkotlincore.sample.presentation.base.BasePresenterImpl
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject
import kotlin.coroutines.experimental.suspendCoroutine


/**
 * Created by Peter on 04.02.2018.
 */
class LoginPresenterImpl : BasePresenterImpl<LoginPresenter, LoginView>(), LoginPresenter {
    @Inject
    protected lateinit var context: Context

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("524265820985-3bk8dq8q1us75a750qrk7a8iqa8m5rpi.apps.googleusercontent.com")
                .requestEmail()
                .build()
        GoogleSignIn.getClient(context, gso)
    }

    init {
        DI.component.inject(this)
    }

    override fun onCreated() {
        super.onCreated()


    }

    override fun signInByGoogle() {
        launchUI {
            try {
                val intent = googleSignInClient.signInIntent
                getView().startActivityForResult(intent, REQUEST_CODE_GOOGLE_SIGN_IN)
                val result = onActivityResult.awaitFirst { it.requestCode == REQUEST_CODE_GOOGLE_SIGN_IN }
                val googleSignInAccount = GoogleSignIn.getSignedInAccountFromIntent(result.data).result

                val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)

                val authResult = suspendCoroutine<AuthResult> { continuation ->
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener({
                        if (it.isSuccessful) {
                            continuation.resume(it.result)
                        } else {
                            continuation.resumeWithException(it.exception ?: IllegalStateException("firebaseAuth.signInWithCredential unknown error"))
                        }
                    })
                }
                val user = authResult.user
                postToView { showMessage("Done! Hi, ${user.displayName} (${user.email})") }
            } catch (e: Exception) {
                e.printStackTrace()
                postToView { showMessage("Oops! ${e.message}") }
            }
        }
    }

    override fun logout() {
        firebaseAuth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener {
            postToView { showMessage("Logout done!") }
        }
    }

    private companion object {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 1
    }
}