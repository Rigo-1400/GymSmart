package com.example.gymsmart.components.pages

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gymsmart.components.nonfirebasepages.MainActivity_NonFireBase

/**
 * Login
 *
 * @param onGoogleSignInClick
 * @receiver
 */
@Composable
fun LoginPage(onGoogleSignInClick: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onGoogleSignInClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login with Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Start MainActivity_NonFirebase
                val intent = Intent(context, MainActivity_NonFireBase::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Video Library")
        }
    }
}
