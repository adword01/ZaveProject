package com.assignment.zaveproject.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.assignment.zaveproject.ui.home.MainActivity
import com.assignment.zaveproject.data.local.UserPrefs
import com.assignment.zaveproject.data.remote.GoogleAuth

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authClient = GoogleAuth(this)
        val viewModel = AuthViewModel(authClient, UserPrefs(this))


        setContent {
            MaterialTheme {
                Surface {
                    LoginScreen(
                        viewModel = viewModel,
                        authState = authClient,
                        onSignInSuccess = {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    )
                }
            }
        }
    }
}