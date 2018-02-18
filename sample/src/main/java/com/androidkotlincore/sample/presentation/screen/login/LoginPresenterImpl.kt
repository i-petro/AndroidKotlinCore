package com.androidkotlincore.sample.presentation.screen.login

import android.content.Context
import com.androidkotlincore.mvp.impl.launchCoroutineUI
import com.androidkotlincore.sample.core.di.DI
import com.androidkotlincore.sample.domain.interactors.LoginInteractor
import com.androidkotlincore.sample.presentation.base.BasePresenterImpl
import javax.inject.Inject


/**
 * Created by Peter on 04.02.2018.
 */
@Suppress("ProtectedInFinal")
class LoginPresenterImpl : BasePresenterImpl<LoginPresenter, LoginView>(), LoginPresenter {
    @Inject
    protected lateinit var context: Context
    @Inject
    protected lateinit var loginInteractor: LoginInteractor

    init {
        DI.component.inject(this)
    }

    override fun signInWithGoogle() {
        launchCoroutineUI {
            try {
                val signInModel = loginInteractor.login(
                        activityForResultStarter = { intent, requestCode ->
                            postToView { startActivityForResult(intent, requestCode) }
                        },
                        activityResultListener = onActivityResult
                )
                postToView { showMessage("Hi, ${signInModel.displayName} (${signInModel.email})") }
            } catch (e: Exception) {
                e.printStackTrace()
                postToView { showMessage("Oops! ${e.message}") }
            }
        }
    }

    override fun logout() {
        launchCoroutineUI {
            try {
                loginInteractor.logout()
                postToView { showMessage("Bue!") }
            } catch (e: Exception) {
                e.printStackTrace()
                postToView { showMessage("Oops! ${e.message}") }
            }
        }
    }
}