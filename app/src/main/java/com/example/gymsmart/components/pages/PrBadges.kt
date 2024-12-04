package com.example.gymsmart.components.pages
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gymsmart.R


@Composable
private fun MyCustomDialog() {
    var openAlert = remember {
        mutableStateOf(false)
    }


    Button(
        onClick = { openAlert.value = true },
        modifier = Modifier
            .width(200.dp)
            .height(1000.dp)

    ) {
        Text(text = "Click me!")
    }

    if (openAlert.value) {
        CustomDialogUI(openAlert)
    }


}

@Composable
private fun CustomDialogUI(openDialogBox: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialogBox.value = false }) {
        CustomUI(openDialogBox)
    }


}

@Composable
private fun CustomUI(openDialog: MutableState<Boolean>) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 5.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.background(Color.White)
        ) {

            /**Image*/
            Image(
                painter = painterResource(id = R.drawable.gymblck),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = Color.Magenta
                ),
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth()
            )

            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Get Updates",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = "Allow permisson to send notifications when new update added on play store!",

                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp, 25.dp, 25.dp, 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium

                )


            }
            /** Buttons*/
            Row(
                Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(
                        text = "Not now", fontWeight = FontWeight.Bold, color = Color.Blue,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }

                TextButton(onClick = { openDialog.value = false }) {
                    Text(
                        text = "Allow", fontWeight = FontWeight.Bold, color = Color.Black,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }

            }


        }


    }

}

