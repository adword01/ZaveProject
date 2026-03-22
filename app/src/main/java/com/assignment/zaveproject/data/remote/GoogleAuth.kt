package com.assignment.zaveproject.data.remote

import android.content.Context
import android.content.Intent
import com.assignment.zaveproject.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import kotlin.getValue

class GoogleAuth(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val googleSigninClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context,gso)
    }

    fun getSignInIntent(): Intent{
        return googleSigninClient.signInIntent
    }


    suspend fun signInWithIntent(intent: Intent): FirebaseUser? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        val account = task.getResult(ApiException::class.java)

        val credential = GoogleAuthProvider.getCredential(
            account.idToken,
            null
        )

        val result = auth.signInWithCredential(credential).await()
        return result.user
    }

    fun signOut() {
        auth.signOut()
        googleSigninClient.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

}