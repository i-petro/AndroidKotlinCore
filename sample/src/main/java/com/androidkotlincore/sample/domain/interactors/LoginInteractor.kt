package com.androidkotlincore.sample.domain.interactors

import android.content.Intent
import com.androidkotlincore.mvp.OnActivityResultEvent
import com.androidkotlincore.mvp.addons.CompositeEventListener
import com.androidkotlincore.sample.domain.model.SignInModel

/**
 * Created by Peter on 05.02.2018.
 */
interface LoginInteractor {
    suspend fun login(activityForResultStarter: (intent: Intent, requestCode: Int) -> Unit,
                      activityResultListener: CompositeEventListener<OnActivityResultEvent>): SignInModel
    suspend fun logout()
}