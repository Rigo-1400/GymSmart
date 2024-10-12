package com.example.gymsmart.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FilterDropdownMenu(
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Filter Workouts"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopStart)
        ) {
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    onFilterSelected("All")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Sort by Date (Newest)") },
                onClick = {
                    onFilterSelected("Sort by Date (Newest)")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Sort by Date (Oldest)") },
                onClick = {
                    onFilterSelected("Sort by Date (Oldest)")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Upper Body") },
                onClick = {
                    onFilterSelected("Upper Body")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Lower Body") },
                onClick = {
                    onFilterSelected("Lower Body")
                    expanded = false
                }
            )
        }
    }
}
