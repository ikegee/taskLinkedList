package com.example.android.tasklinkedlist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.tasklinkedlist.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Project: TaskLinkedList File: AddTaskActivity.java
 * Date: Feb. 15, 2017 Time: 10:34:43 PM
 * Author: G.E. Eidsness
 * Updated: 2023-12-10  AddTaskActivity.kt
 * Modified: 2024-02-10
 */
class AddTaskActivity : AppCompatActivity(), View.OnClickListener {

    private var taskDate: Button? = null
    private var taskTitle: EditText? = null
    private var taskDescription: EditText? = null
    private var taskCheckBox: CheckBox? = null
    private var spCategory: Spinner? = null
    private var spPriority: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        spCategory = findViewById<View>(R.id.spinner_category) as Spinner
        spPriority = findViewById<View>(R.id.spinner_priority) as Spinner
        taskTitle = findViewById<View>(R.id.task_title) as EditText
        taskDescription = findViewById<View>(R.id.taskDescription) as EditText
        taskDate = findViewById<View>(R.id.button_task_date) as Button
        taskDate!!.text = Date().toString()  // incoming: SAT FEB 10 01:09:40 PST 2024
        taskDate!!.setOnClickListener { _: View? ->
            val manager = supportFragmentManager
            val newFragment: DialogFragment = TaskDatePicker()
            newFragment.show(manager, DIALOG_DATE)
        }
        taskTitle!!.requestFocus()
        if(taskTitle!!.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
        taskCheckBox = findViewById<View>(R.id.chkTask) as CheckBox
        taskCheckBox!!.setOnClickListener {
            if (taskCheckBox!!.isChecked) {
                isChecked = true
                taskCheckBox!!.setText(R.string.task_checked_completed)
            } else {
                isChecked = true
                taskCheckBox!!.setText(R.string.task_checked_pending)
            }
        }

        /* Category Menu */
        val adapterCat = ArrayAdapter.createFromResource(
            this,
            R.array.category_array, android.R.layout.simple_spinner_item
        )
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Log.d(TAG, "adapterCat:$adapterCat")
        Log.d(TAG, spCategory.toString())
        spCategory!!.adapter = adapterCat
        spCategory!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val items = resources.getStringArray(R.array.category_array)
                theCategory = items[position]
                //Toast.makeText(AddTaskActivity.this, "Selected : " + theCategory, Toast.LENGTH_SHORT).show();
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                theCategory = "Home"
            }
        }
        /* End Category Menu */

        /* Priority Menu */
        val adapterPriority = ArrayAdapter.createFromResource(
            this,
            R.array.priority_array, android.R.layout.simple_spinner_item
        )
        adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPriority!!.adapter = adapterPriority
        spPriority!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val items = resources.getStringArray(R.array.priority_array)
                thePriority = items[position]
                //Toast.makeText(AddTaskActivity.this, "Selected : " + thePriority, Toast.LENGTH_SHORT).show();
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                thePriority = "Low"
            }
        }
        /* End Priority Menu */
        val btnAdd = findViewById<View>(R.id.button_add_task) as Button
        btnAdd.setOnClickListener(this@AddTaskActivity)
    }

    // Perform Operation to Add item to Data Set.
    // notifyItemInserted(position) method on adapter
    override fun onClick(view: View) {
        if (view.id == R.id.button_add_task) {
            theTitle = taskTitle!!.text.toString()
            if (theTitle!!.isEmpty()) {
                Toast.makeText(applicationContext, "Enter Title", Toast.LENGTH_SHORT).show()
                taskTitle!!.text = null
                taskTitle!!.hint = "Enter Title"
                return
            }
            theDescription = taskDescription!!.text.toString()
            if (theDescription!!.isEmpty()) {
                Toast.makeText(applicationContext, "Enter Description", Toast.LENGTH_SHORT).show()
                return
            }
            theDate = taskDate!!.text.toString()
            theCategory = spCategory!!.selectedItem.toString()
            thePriority = spPriority!!.selectedItem.toString()
            isChecked = taskCheckBox!!.isChecked
            populateAndSaveDataFromSingleton()
            returnHome()
        } else {
            returnHome()
        }
    }

    private fun populateAndSaveDataFromSingleton() {
        val task = Task()
        task.title = theTitle
        task.description = theDescription
        task.date = theDate?.let { formatDate(it) }
        task.category = theCategory
        task.priority = thePriority
        task.isTasked = isChecked
        taskInstance.addTask(task)
    }

     private fun returnHome() {
        val mainIntent = Intent(applicationContext, DisplayTasksActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(mainIntent)
        finish()
    }

    companion object {
        val TAG: String = AddTaskActivity::class.java.simpleName
        private const val DIALOG_DATE = "DialogDate"
        private val taskInstance = TaskList.getInstance()
        private var theTitle: String? = null
        private var theDate: String? = null
        private var theDescription: String? = null
        private var theCategory: String? = null
        private var thePriority: String? = null
        private var isChecked: Boolean = false
        private var myDate: Date? = null

        // incoming: FRI FEB 09 13:30:40 PST 2024
        @SuppressLint("SimpleDateFormat")
        private fun formatDate(strDate: String): Date? {
            val dateFORMAT = "EEEE MMM dd HH:mm:ss Z yyyy" //"EEE MMM dd HH:mm:ss zzz yyyy"
            val sdf = java.text.SimpleDateFormat(dateFORMAT, Locale.CANADA)
            val regex = "\\d{4}-\\d{2}-\\d{2}".toRegex()

            if (strDate.matches(regex)) {
                // Parse short date
                val sdfShort = android.icu.text.SimpleDateFormat("yyyy-MM-dd")
                val shortDate = sdfShort.parse(strDate)

                // Get day of week
                val dayOfWeek = SimpleDateFormat("EEEE", Locale.CANADA).format(shortDate)
                // Create Calendar
                val cal = Calendar.getInstance()
                cal.time = shortDate

                // Set time fields
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)

                // Set timezone
                cal.timeZone = TimeZone.getTimeZone("PST")

                // Format to full string
                val formatted = sdf.format(shortDate)

                // Set to Friday in example
                formatted.replace(formatted.substring(0,3), dayOfWeek)

               myDate = sdf.parse(formatted)

            } else {
                // original parsing
                try {
                    myDate = sdf.parse(strDate)
                } catch (e: java.text.ParseException) {
                    e.printStackTrace()
                    Log.e(TAG, "formatDate: $e")
                }
            }
            return myDate
        }
    }
}
