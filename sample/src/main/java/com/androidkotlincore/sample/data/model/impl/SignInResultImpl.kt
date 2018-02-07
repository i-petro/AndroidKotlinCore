package com.androidkotlincore.sample.data.model.impl

import com.androidkotlincore.sample.domain.model.SignInModel

/**
 * Created by Peter on 06.02.18.
 */
data class SignInResultImpl(
        override val isNewUser: Boolean,
        override val userName: String?,
        override val providerId: String,
        override val email: String?,
        override val displayName: String?,
        override val uid: String,
        override val phoneNumber: String?
): SignInModel