package com.androidkotlincore.sample.presentation.screen.login

import android.content.Context
import com.androidkotlincore.mvp.addons.awaitFirst
import com.androidkotlincore.mvp.impl.launchUI
import com.androidkotlincore.sample.core.di.DI
import com.androidkotlincore.sample.domain.interactors.LoginInteractor
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
    @Inject
    protected lateinit var loginInteractor: LoginInteractor

    init {
        DI.component.inject(this)
    }

    override fun signInWithGoogle() {
        launchUI {

            try {
                val signInModel = loginInteractor.login(
                        startActivityForResult = {intent, requestCode -> postToView { startActivityForResult(intent, requestCode) } },
                        getActivityResult = onActivityResult
                )
                postToView { showMessage("Done! Hi, ${signInModel.displayName} (${signInModel.email})") }
            } catch (e: Exception) {
                e.printStackTrace()
                postToView { showMessage("Oops! ${e.message}") }
            }
        }
    }

    override fun logout() {
//        firebaseAuth.signOut()
//
//        // Google sign out
//        googleSignInClient.signOut().addOnCompleteListener {
//            postToView { showMessage("Logout done!") }
//        }
    }

    private companion object {

    }
}