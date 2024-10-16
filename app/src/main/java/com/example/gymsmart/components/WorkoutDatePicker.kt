package com.example.gymsmart.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymsmart.firebase.WorkoutData
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Date

@Composable

fun WorkoutDatePicker(context: Context, onDateSelected: (String) -> Unit, workouts: List<WorkoutData>) {
    val year: Int
    val month: Int
    val day: Int



    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    month = calendar.get(Calendar.MONTH)

    calendar.time = Date()

    val date = remember { mutableStateOf("") }
    workouts.forEach { workout ->
        Text("Workout date: ${workout.dateAdded}")
    }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, dayOfMonth: Int ->
            val formattedDate = "$dayOfMonth/${selectedMonth + 1}/$selectedYear"  // Month is 0-indexed
            date.value = "Workouts for selected date: $formattedDate"

           // onDateSelected(formattedDate)
        }, year, month, day
    )



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
