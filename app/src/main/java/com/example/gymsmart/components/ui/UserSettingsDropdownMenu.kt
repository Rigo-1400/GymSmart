package com.example.gymsmart.components.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gymsmart.firebase.UserSession
import com.example.gymsmart.firebase.FirebaseAuthHelper

@Composable
fun UserSettingsDropdownMenu(
    onFilterSelected: (String) -> Unit,
    firebaseAuthHelper: FirebaseAuthHelper,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = true }) {
            AsyncImage(UserSession.userPhotoUrl, "${UserSession.userName} Profile Picture")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopStart)
        ) {
            DropdownMenuItem(
                text = { Text("Settings") },
                onClick = {
                    onFilterSelected("Settings")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Logout") },
                onClick = {
                    // TODO: Figure out a way to call the firebaseAuthHelper.signOut function here.
                    expanded = false
                    firebaseAuthHelper.signOut()
                    navController.navigate("login")
                }
            )
        }
    }
}
