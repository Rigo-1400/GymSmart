package com.example.gymsmart.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymsmart.firebase.WorkoutData
import java.util.Calendar

@Composable
fun WorkoutDatePicker(
    context: Context,
    onDateSelected: (String) -> Unit,
    workouts: List<WorkoutData>
) {
    // Get the current date
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // State to hold the selected date
    val date = remember { mutableStateOf("") }

    // DatePickerDialog for selecting a date
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, dayOfMonth: Int ->
            val formattedDate = "$dayOfMonth/${selectedMonth + 1}/$selectedYear"  // Formatting the selected date
            onDateSelected(formattedDate)  // Pass the formatted date back to the parent
        }, year, month, day
    )


    // UI for the DatePicker
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = date.value)
        Button(onClick = { datePickerDialog.show() }) {
            Text(text = "Open Calendar")
        }
    }
}
