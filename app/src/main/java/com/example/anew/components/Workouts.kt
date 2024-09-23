package com.example.anew.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
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
// TODO: Add the ability to take in the Workouts parameter that will be an interface/object
fun Workouts(navController: NavController, workouts: Array<String>) {

    // Creating two State variables
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    // Filter the workouts based on the search query
    val filteredWorkouts = workouts.filter {
        it.contains(text, ignoreCase = true)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between items
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = text,
                onQueryChange = { text = it },
                onSearch = { active = false },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("Search for workout") },
                leadingIcon = { Icon(Icons.Default.Search, "Search Icon") },
                trailingIcon = {
                    if (active) {
                        Icon(
                            modifier = Modifier.clickable {
                                if (text.isNotEmpty()) {
                                    text = ""
                                } else active = false
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon"
                        )
                    }
                }
            ) {
                // Show the filtered list of workouts inside the search bar
                filteredWorkouts.forEach { workout ->
                    Row(modifier = Modifier.padding(all = 14.dp).clickable { navController.navigate("workout/$workout") }) {
                        Text(text = workout)
                    }
                }
            }
            // Show filtered workouts below the search bar as buttons
            filteredWorkouts.forEach { workoutName ->
                Button(
                    onClick = { navController.navigate("workout/$workoutName") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(workoutName)
                }
            }
        }
    }
}

