package com.example.gymsmart.components.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowDown
import com.composables.icons.lucide.Lucide

@Composable
fun MuscleGroupSelectorDropdownMenu(
    onMuscleGroupSelected: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedMuscleGroup by remember { mutableStateOf("Select Muscle Group") }

    Box(modifier = Modifier.fillMaxWidth()) {
        // Trigger DropdownMenu via TextField
        TextField(
            value = selectedMuscleGroup,
            onValueChange = { },
            readOnly = true,
            label = { Text("Muscle Group") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clickable { expanded = !expanded },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Lucide.ArrowDown, contentDescription = "Dropdown")
                }
            },
//            colors = TextFieldDefaults.textFieldColors(
//                disabledIndicatorColor = MaterialTheme.colorScheme.onSurface,
//                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface
//            )
        )

        // Dropdown Menu with Headers and Items
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Upper Body Header
            Text(
                text = "Upper Body",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(8.dp)
            )
            DropdownMenuItem(
                text = { Text("Chest") },
                onClick = {
                    selectedMuscleGroup = "Chest"
                    onMuscleGroupSelected("Chest", "Upper Body")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Triceps") },
                onClick = {
                    selectedMuscleGroup = "Triceps"
                    onMuscleGroupSelected("Triceps", "Upper Body")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Biceps") },
                onClick = {
                    selectedMuscleGroup = "Biceps"
                    onMuscleGroupSelected("Biceps", "Upper Body")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Shoulders") },
                onClick = {
                    selectedMuscleGroup = "Shoulders"
                    onMuscleGroupSelected("Shoulders", "Upper Body")
                    expanded = false
                }
            )

            HorizontalDivider() // Separator between sections

            // Lower Body Header
            Text(
                text = "Lower Body",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(8.dp)
            )
            DropdownMenuItem(
                text = { Text("Quadriceps") },
                onClick = {
                    selectedMuscleGroup = "Quadriceps"
                    onMuscleGroupSelected("Quadriceps", "Lower Body")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Hamstrings") },
                onClick = {
                    selectedMuscleGroup = "Hamstrings"
                    onMuscleGroupSelected("Hamstrings", "Lower Body")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Calves") },
                onClick = {
                    selectedMuscleGroup = "Calves"
                    onMuscleGroupSelected("Calves", "Lower Body")
                    expanded = false
                }
            )
        }
    }
}
