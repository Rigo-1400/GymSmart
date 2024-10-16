package com.example.gymsmart.components.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Login
 *
 * @param onGoogleSignInClick
 * @receiver
 */
@Composable
fun LoginPage(onGoogleSignInClick: () -> Unit) {
    Button(
        onClick = onGoogleSignInClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Login with Google")
    }
}
