package com.example.gymsmart.components.ui

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Lucide
import java.util.Calendar

@Composable
fun WorkoutDatePicker(
    context: Context,
    onDateSelected: (String) -> Unit,
) {
    // Get the current date
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // DatePickerDialog for selecting a date
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, dayOfMonth: Int ->
            val formattedDate = "$dayOfMonth/${selectedMonth + 1}/$selectedYear"  // Formatting the selected date
            onDateSelected(formattedDate)  // Pass the formatted date back to the parent
        }, year, month, day
    )


    // UI for the DatePicker
    Box(Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton({ datePickerDialog.show() }) {
            Icon(Lucide.Calendar, "Open Calender Filter")
        }
    }
}
