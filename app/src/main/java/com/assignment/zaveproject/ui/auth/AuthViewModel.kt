package com.assignment.zaveproject.ui.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.zaveproject.data.local.UserPrefs
import com.assignment.zaveproject.data.remote.GoogleAuth
import com.assignment.zaveproject.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val googleAuth: GoogleAuth,
    private val userPrefs: UserPrefs
): ViewModel() {

    private val state = MutableStateFlow(AuthState())
    val authState : StateFlow<AuthState> = state


    fun onEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.OnSignInClick -> {
            }

            is AuthEvent.OnSignInResult -> {
                handleSignInResult(event.intent)
            }
        }
    }

    private fun handleSignInResult(intent: Intent) {
        viewModelScope.launch {
            state.update {
                it.copy(isLoading = true)
            }
            try{
                val firebaseUser = googleAuth.signInWithIntent(intent)

                firebaseUser.let {
                    val user = User(
                        name = it?.displayName ?: "",
                        email = it?.email ?: "",
                        profileImage = it?.photoUrl.toString()
                    )

                    userPrefs.saveUserData(user)

                    state.update {
                        it.copy(
                            isLoading = false,
                            user = user
                        )
                    }
                }
            } catch (e: Exception){
                state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }




}