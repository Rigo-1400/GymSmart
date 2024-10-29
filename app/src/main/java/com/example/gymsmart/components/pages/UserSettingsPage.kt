package com.example.gymsmart.components.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.composables.icons.lucide.Cog
import com.composables.icons.lucide.Dumbbell
import com.composables.icons.lucide.LogOut
import com.composables.icons.lucide.MoveLeft
import com.composables.icons.lucide.Lucide
import com.example.gymsmart.firebase.FirebaseAuthHelper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsPage(
    navController: NavController,
    firebaseAuthHelper: FirebaseAuthHelper) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Lucide.MoveLeft,
                            contentDescription = "Move Back Previous Page"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // List item for navigating to the Users Gym Accessories screen
                SettingsListItem(
                    text = "Accessories",
                    description = "View or edit your gym accessories",
                    icon = Lucide.Dumbbell,
                    onClick = { navController.navigate("profile") }
                )

                // List item for navigation to the Users Gym machine settings screen
                SettingsListItem(
                    text = "Gym Machine Settings",
                    description = "View or edit your gym machine settings",
                    icon = Lucide.Cog,
                    onClick = { navController.navigate("machine_settings") }
                )

                // List item for logging out
                SettingsListItem(
                    text = "Logout",
                    description = "Sign out of your account",
                    icon = Lucide.LogOut,
                    onClick = {
                        firebaseAuthHelper.signOut()
                        navController.navigate("login")
                    }
                )
            }
        }
    )
}
@Composable
fun SettingsListItem(
    text: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(text) },
        supportingContent = { Text(description) },
        leadingContent = { Icon(icon, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    )
    HorizontalDivider()
}
