package com.assignment.zaveproject.ui.auth

import android.content.Intent

sealed class AuthEvent {
    object OnSignInClick : AuthEvent()
    data class OnSignInResult(val intent: Intent) : AuthEvent()
}