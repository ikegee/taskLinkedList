package com.example.android.tasklinkedlist

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.tasklinkedlist.R
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

/**
 * Author:  G.E. Eidsness
 * Project: TaskLinkedList File: TaskDatePicker.java
 * Created:  2017-08-15;
 * Updated : 2023-12-10 TaskDatePicker.kt
 */

class TaskDatePicker : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the date picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val taskDate = requireActivity().findViewById<Button>(R.id.button_task_date)
        val date = LocalDate.of(year, month + 1, dayOfMonth) // Month is 0-based in Java
        taskDate.text = date.toString()
        sendResult(date)
    }

    private fun sendResult(date: LocalDate) {
        // Convert LocalDate to Date
        val instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val dateForTask = Date.from(instant)
        requireActivity().supportFragmentManager
            .setFragmentResult("requestKey", Bundle().apply {
                putSerializable("taskDate", dateForTask)
            })
    }

}
