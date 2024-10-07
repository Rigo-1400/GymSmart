package com.example.gymsmart.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun Login(onGoogleSignInClick: () -> Unit) {
    Button(
        onClick = onGoogleSignInClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Login with Google")
    }
}
