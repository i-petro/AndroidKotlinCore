package com.androidkotlincore.sample.data.service.impl

import android.content.Context
import com.androidkotlincore.sample.R
import com.androidkotlincore.sample.data.service.GoogleSignInService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

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


}