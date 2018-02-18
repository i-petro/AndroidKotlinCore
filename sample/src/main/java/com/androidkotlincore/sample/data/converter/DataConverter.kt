package com.androidkotlincore.sample.data.converter

import com.androidkotlincore.entityconverter.ConvertersContext
import com.androidkotlincore.entityconverter.ConvertersContextRegistrationCallback
import com.androidkotlincore.entityconverter.registerConverter
import com.androidkotlincore.sample.data.model.impl.SignInResultImpl
import com.google.firebase.auth.AuthResult

/**
 * Created by Peter on 06.02.18.
 */
class DataConverter : ConvertersContextRegistrationCallback {

    override fun register(convertersContext: ConvertersContext) {
        convertersContext.registerConverter(this::googleAuthResultToSignInResult)
    }

    private fun googleAuthResultToSignInResult(authResult: AuthResult): SignInResultImpl {
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