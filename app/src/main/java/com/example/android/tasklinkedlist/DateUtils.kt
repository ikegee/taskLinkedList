package com.example.android.tasklinkedlist

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.util.Log
import java.text.ParseException
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal class DateUtils {

    companion object {

        @SuppressLint("SimpleDateFormat")
        private fun parseShortDate(strDate: String): Date? {

            // Parse short date "2024-02-13" to full date "Tue Feb 13 00:00:00 PST 2024"
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA)
            val shortDate = sdf.parse(strDate)

            // Initialize cal with shortDate
            val cal = Calendar.getInstance()
            if (shortDate != null) {
                cal.time = shortDate
            }

            // Set time fields to 12:00:00 no matter what the date is
            cal.set(Calendar.HOUR_OF_DAY, 8)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)

            // Set timezone
            cal.timeZone = TimeZone.getTimeZone("PST")

            // Format to full date/time string
            val formattedSdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.CANADA)
            val formatted = formattedSdf.format(cal.time)
            Log.d(EditTaskActivity.TAG, "Formatted: $formatted")
            return formattedSdf.parse(formatted)
        }

        // incoming: Sat Feb 10 01:09:40 PST 2024
        @SuppressLint("SimpleDateFormat")
        fun formatDate(strDate: String): Date? {

            SimpleDateFormat("yyyy-MM-dd", Locale.CANADA)
            val longFormat = SimpleDateFormat("EEEE MMM dd HH:mm:ss Z yyyy", Locale.CANADA)
            val defaultFormat = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.CANADA)

            if (strDate.matches("\\d{4}-\\d{2}-\\d{2}".toRegex())) {
                return parseShortDate(strDate)
            }

            val date = try {
                longFormat.parse(strDate)
            } catch (e: ParseException) {
                // failed, try default format
                try {
                    defaultFormat.parse(strDate)
                } catch (e: ParseException) {
                    // Both formats failed, handle error or log it
                    e.printStackTrace()
                    Log.e(AddTaskActivity.TAG, "formatDate: $e")
                    return null
                }
            }
            return date
        }
    }
}