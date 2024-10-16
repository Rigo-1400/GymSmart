package com.example.gymsmart.components.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.X

@Composable
fun SearchBarWithIcon(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    var isSearchBarVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // If the search bar is visible, show the TextField
        if (isSearchBarVisible) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                label = { Text("Search for a workout") },
                leadingIcon = {
                    Icon(
                        imageVector = Lucide.Search,
                        contentDescription = "Search Icon"
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Lucide.X,
                        contentDescription = "Clear Search",
                        modifier = Modifier.clickable {
                            onSearchQueryChanged("")
                            isSearchBarVisible = false // Hide the search bar when cleared
                        }
                    )
                },
                modifier = Modifier
                    .weight(1f) // Makes the TextField take up available space
                    .padding(end = 8.dp)
                    .focusRequester(focusRequester),
                singleLine = true
            )

            // Automatically request focus when the search bar becomes visible
            LaunchedEffect(isSearchBarVisible) {
                if (isSearchBarVisible) {
                    focusRequester.requestFocus()
                }
            }

            // Automatically hide the search bar when focus is lost
            DisposableEffect(Unit) {
                onDispose {
                    isSearchBarVisible = false
                }
            }
        } else {
            // If the search bar is not visible, show the search icon
            Icon(
                imageVector = Lucide.Search,
                contentDescription = "Search Icon",
                modifier = Modifier
                    .clickable {
                        isSearchBarVisible = true // Show the search bar on click
                    }
                    .padding(8.dp)
            )
        }
    }
}
