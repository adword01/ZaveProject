package com.assignment.zaveproject.ui.auth

import com.assignment.zaveproject.domain.model.User

data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)