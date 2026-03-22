package com.assignment.zaveproject.ui.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assignment.zaveproject.data.remote.GoogleAuth

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    authState: GoogleAuth,
    onSignInSuccess: () -> Unit
) {

    val state by viewModel.authState.collectAsState()

    LaunchedEffect(key1 = state.user) {
        if (state.user != null) {
            onSignInSuccess()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){ result ->
        result.data?.let {
            viewModel.onEvent(AuthEvent.OnSignInResult(it))
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Zave Shopping",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                val intent = authState.getSignInIntent()
                launcher.launch(intent)
            }) {
                Text(text = "Sign in with Google")
            }
            if (state.isLoading){
                Spacer(modifier = Modifier.height(14.dp))
                CircularProgressIndicator()
            }
            state.error?.let {
                Text(it)
            }
        }
    }


}
