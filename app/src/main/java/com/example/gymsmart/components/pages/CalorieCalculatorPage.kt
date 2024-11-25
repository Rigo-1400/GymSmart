package com.example.gymsmart.components.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalorieCalculatorPage() {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var activityLevel by remember { mutableStateOf(1.2f) }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Calorie Calculator", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Add dropdown for gender
        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            result = calculateCalories(weight.toFloatOrNull(), height.toFloatOrNull(), age.toIntOrNull(), gender, activityLevel)
        }) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Calories Needed: $result")
    }
}

fun calculateCalories(weight: Float?, height: Float?, age: Int?, gender: String, activityLevel: Float): String {
    if (weight == null || height == null || age == null) return "Fill in all fields"

    val bmr = if (gender == "Male") {
        10 * weight + 6.25 * height - 5 * age + 5
    } else {
        10 * weight + 6.25 * height - 5 * age - 161
    }
    return (bmr * activityLevel).toInt().toString()
}
