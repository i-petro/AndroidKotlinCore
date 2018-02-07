package com.androidkotlincore.sample.domain.model

/**
 * Created by Peter on 06.02.18.
 */
interface SignInModel {
    val isNewUser: Boolean
    val userName: String?
    val providerId: String
    val email: String?
    val displayName: String?
    val uid: String
    val phoneNumber: String?
}