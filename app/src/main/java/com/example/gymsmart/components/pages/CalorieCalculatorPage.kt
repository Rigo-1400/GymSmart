package com.example.gymsmart.components.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieCalculatorPage(navController: NavController) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var activityLevel by remember { mutableStateOf(1.2f) }
    var result by remember { mutableStateOf("Enter details to calculate") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val genders = listOf("Male", "Female")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calorie Calculator",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions.Default
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions.Default
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions.Default
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = gender,
                onValueChange = { gender = it },
                label = { Text("Gender") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genders.forEach { genderOption ->
                    DropdownMenuItem(
                        text = { Text(genderOption) },
                        onClick = {
                            gender = genderOption
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Activity Level: ${String.format("%.1f", activityLevel)}", color = Color.White)
        Slider(
            value = activityLevel,
            onValueChange = { activityLevel = it },
            valueRange = 1f..2f,
            steps = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val weightValue = weight.toFloatOrNull()
            val heightValue = height.toFloatOrNull()
            val ageValue = age.toIntOrNull()

            if (weightValue == null || heightValue == null || ageValue == null) {
                errorMessage = "Please enter valid numbers for weight, height, and age."
            } else {
                val calculatedCalories = calculateCalories(
                    weight = weightValue,
                    height = heightValue,
                    age = ageValue,
                    gender = gender,
                    activityLevel = activityLevel
                )
                result = calculatedCalories
                errorMessage = null
            }
        }) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Displaying the result directly below the button
        Text("Calories Needed: $result", color = Color.White)
    }
}

fun calculateCalories(weight: Float?, height: Float?, age: Int?, gender: String, activityLevel: Float): String {
    return if (weight != null && height != null && age != null) {
        val bmr = if (gender == "Male") {
            10 * weight + 6.25 * height - 5 * age + 5
        } else {
            10 * weight + 6.25 * height - 5 * age - 161
        }
        (bmr * activityLevel).toInt().toString()
    } else {
        "Invalid input. Please check your values."
    }
}
