package com.example.gymsmart.components.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage // Import this for AsyncImage support
import com.composables.icons.lucide.Dumbbell
import com.composables.icons.lucide.LogOut
import com.composables.icons.lucide.MoveLeft
import com.composables.icons.lucide.Lucide
import com.example.gymsmart.firebase.FirebaseAuthHelper
import com.example.gymsmart.firebase.UserSession
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.gymsmart.R

/**
 * User Settings Page
 *
 * @param navController
 * @param firebaseAuthHelper
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsPage(
    navController: NavController,
    firebaseAuthHelper: FirebaseAuthHelper
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Lucide.MoveLeft,
                            contentDescription = "Move Back to Previous Page"
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally


            ) {
                // Profile picture


                // User information
                Text(
                    text = "${UserSession.userName}",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 25.sp)
                )
                Text(
                    text = UserSession.userEmail ?: "No Email", // Replace with your actual user data
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    ),
                    color = Color.Gray
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.White
                )



                Spacer(modifier = Modifier.height(16.dp))

                // Optional Async Image for user profile from URL
                AsyncImage(
                    model = UserSession.userPhotoUrl, // Assumes URL is provided
                    contentDescription = "${UserSession.userName} Profile Picture",
                    modifier = Modifier
                        .size(125.dp)
                        .clip(CircleShape)
                )
                //Part for badges here
                Column {
                    Row {
                        Image(painter = painterResource(id = R.drawable.image1), contentDescription = null,
                            modifier = Modifier
                                .alignByBaseline()
                                .size(50.dp))
                        
                        Image(painter = painterResource(id = R.drawable.image3), contentDescription =null,
                            modifier = Modifier
                                .alignByBaseline()
                                .size(50.dp))
                        Image(painter = painterResource(id = R.drawable.image2), contentDescription = null,
                            modifier = Modifier
                                .alignByBaseline()
                                .size(50.dp))
                        Image(painter = painterResource(id = R.drawable.image4), contentDescription = null,
                            modifier = Modifier
                                .alignByBaseline()
                                .size(50.dp))
                        Image(painter = painterResource(id = R.drawable.image5), contentDescription = null,
                            modifier = Modifier
                                .alignByBaseline()
                                .size(50.dp))
                    }
                }


                // List items for user settings
                SettingsListItem(
                    text = "Accessories",
                    description = "View or edit your gym accessories",
                    icon = Lucide.Dumbbell,
                    onClick = { navController.navigate("attachments") }
                )
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