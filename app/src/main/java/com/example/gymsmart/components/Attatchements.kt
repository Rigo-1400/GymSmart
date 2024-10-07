package com.example.anew.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun Attatchements(navController: NavController, attatchements: Array<String>) {

    var text by remember { mutableStateOf("") }
    val filteredAttatchements = attatchements.filter {
        it.contains(text, ignoreCase = true)
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between items
        ){

        filteredAttatchements.forEach { attatchementsNames ->


            Button(
                onClick = { navController.navigate("attatchements2/$attatchementsNames") },  //Change "attatchements/$attatchementsNames" attatchement name to another file to access those buttons (i.e Workout.kt)
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(attatchementsNames)
            }
        }
        }
    }
}