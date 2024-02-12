package com.example.android.tasklinkedlist

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.tasklinkedlist.R
import java.text.ParseException
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

/**
 * Project: TaskLinkedList File: EditTaskActivity.java
 * Date: Feb. 15, 2017 Time: 10:34:43 PM
 * Author: G.E. Eidsness
 * Updated: 2023-12-09 => EditTaskActivity.kt
 * Modified: 2024-02-10
 */
class EditTaskActivity : AppCompatActivity(), View.OnClickListener {

    private var taskTitle: EditText? = null
    private var taskDescription: EditText? = null
    private var taskDate: Button? = null
    private var taskCheckBox: CheckBox? = null
    private var spCategory: Spinner? = null
    private var spPriority: Spinner? = null
    private var taskId: UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        //LinkedList<Task> tskList = getIntent().getParcelableExtra("tskList");
        Log.d(TAG, "hashCode: " + taskInstance.hashCode())
        spCategory = findViewById<View>(R.id.spinner_category) as Spinner
        spPriority = findViewById<View>(R.id.spinner_priority) as Spinner
        taskTitle = findViewById<View>(R.id.task_title) as EditText
        taskDescription = findViewById<View>(R.id.taskDescription) as EditText
        taskDate = findViewById<View>(R.id.button_task_date) as Button
        taskDate!!.setOnClickListener {
            val manager = supportFragmentManager
            val newFragment: DialogFragment = TaskDatePicker()
            newFragment.show(manager, DIALOG_DATE)
            //Toast.makeText(EditTaskActivity.this, "Clicked : " + DIALOG_DATE, Toast.LENGTH_SHORT).show();
        }
        taskCheckBox = findViewById<View>(R.id.chkTask) as CheckBox
        taskCheckBox!!.setOnClickListener {
            if (taskCheckBox!!.isChecked) {
                isChecked = true
                taskCheckBox!!.setText(R.string.task_checked_completed)
            } else {
                isChecked = false
                taskCheckBox!!.setText(R.string.task_checked_pending)
            }
            //Toast.makeText(getApplicationContext(), Boolean.toString(isChecked), Toast.LENGTH_SHORT).show();
        }

        /* Category Menu */
        val adapterEditCat = ArrayAdapter.createFromResource(
            this@EditTaskActivity,
            R.array.category_array, android.R.layout.simple_spinner_item
        )
        adapterEditCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory!!.adapter = adapterEditCat
        spCategory!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, theCategory!!)
                val items = resources.getStringArray(R.array.category_array)
                theCategory = items[position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                setSpinnerValue(spCategory, theCategory)
            }
        }
        /* End Category Menu */

        /* Priority Menu */
        val adapterEditPriority = ArrayAdapter.createFromResource(
            this,
            R.array.priority_array, android.R.layout.simple_spinner_item
        )
        adapterEditPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPriority!!.adapter = adapterEditPriority
        spPriority!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, thePriority!!)
                val items = resources.getStringArray(R.array.priority_array)
                thePriority = items[position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                setSpinnerValue(spPriority, thePriority)
            }
        }
        /* End Priority Menu */
        val bundle = intent.extras
        if (bundle != null && !bundle.isEmpty) {
            //id = getIntent().getExtras().getString("taskID");
            this.taskId = intent.getSerializableExtra("taskID") as UUID?
            val theTitle = intent.extras!!.getString("taskTitle")
            val theDescription = intent.extras!!.getString("taskDescription")
            intent.extras!!.getInt("taskPosition")
            theCategory = intent.extras!!.getString("taskCategory")
            thePriority = intent.extras!!.getString("taskPriority")
            val theDate = intent.extras!!.getString("taskDate")
            isChecked = intent.extras!!.getBoolean("taskStatus")
            taskTitle!!.setText(theTitle)
            taskDescription!!.setText(theDescription)
            setTaskStatus(taskCheckBox, isChecked)
            taskCheckBox!!.isChecked = isChecked
            setSpinnerValue(spCategory, theCategory)
            setSpinnerValue(spPriority, thePriority)
            taskDate!!.text = theDate
        } else {
            returnHome()
        }
        val btnUpdate = findViewById<View>(R.id.button_update) as Button
        val btnDelete = findViewById<View>(R.id.button_delete) as Button
        val btnEmail = findViewById<View>(R.id.button_email) as Button
        btnUpdate.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnEmail.setOnClickListener(this)
    }

    // Modify
    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_update -> {
                // get Task object from the singleton
                val task = taskInstance.getTask(taskId)
                // Get the position passed from DisplayTasksActivity
                val position = intent.getIntExtra("taskPosition", -1)
                // Update the task object with the data from the EditTaskActivity
                if (task != null && position != -1) {
                    task.title = taskTitle!!.text.toString()
                    task.description = taskDescription!!.text.toString()
                    task.date = formatDate(taskDate!!.text.toString())
                    task.category = theCategory
                    task.priority = thePriority
                    task.isTasked = isChecked
                    taskInstance.updateTask(task)
                }
                returnHome()
            }

            R.id.button_delete -> {
                taskInstance.deleteTaskWithId(taskId)
                returnHome()
            }

            R.id.button_email -> emailTaskInfo()
        }
    }

    private fun returnHome() {
        val intent = Intent(this@EditTaskActivity, DisplayTasksActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setTaskStatus(taskCheckBox: CheckBox?, `var`: Boolean) {
        if (`var`) {
            taskCheckBox!!.setText(R.string.task_checked_completed)
        } else taskCheckBox!!.setText(R.string.task_checked_pending)
    }

    private fun setSpinnerValue(spinner: Spinner?, myString: String?) {
        for (i in 0 until spinner!!.count) {
            if (spinner.getItemAtPosition(i).toString() == myString) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun emailTaskInfo() {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(EMAILADDRESS))
        email.putExtra(Intent.EXTRA_SUBJECT, taskTitle!!.text.toString())
        email.putExtra(Intent.EXTRA_TEXT, taskDescription!!.text.toString())
        email.setType("message/rfc822")
        try {
            this.startActivity(Intent.createChooser(email, "Choose an Email client :"))
            EMAILSENT = true
            finish()
            Log.d(TAG, "Message Sent")
        } catch (noSuchActivity: ActivityNotFoundException) {
            noSuchActivity.message
        }
    }

    companion object {
        private val TAG = EditTaskActivity::class.java.simpleName
        private const val DIALOG_DATE = "DialogDate"
        private val taskInstance = TaskList.getInstance()
        private const val EMAILADDRESS = "joeblow@from.idaho.ca"
        private var EMAILSENT = false
        private var theCategory: String? = null
        private var thePriority: String? = null
        private var isChecked: Boolean = false
        private var myDate: Date? = null

        @SuppressLint("SimpleDateFormat")
        private fun parseShortDate(format: SimpleDateFormat, strDate: String): Date? {
            val date = format.parse(strDate)
            //... code to handle short date
            // Parse short date
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA) // 2018-02-20
            val shortDate = sdf.parse(strDate)

            // Get day of week
            val dayOfWeek = SimpleDateFormat("EEEE", Locale.US).format(shortDate)

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
            formatted.replace(formatted.substring(0, 3), dayOfWeek)
            Log.d(TAG, "Formatted: $formatted")
            myDate = sdf.parse(formatted)

            return date
        }
    }

    // incoming: Sat Feb 10 01:09:40 PST 2024
    @SuppressLint("SimpleDateFormat")
    private fun formatDate(strDate: String): Date? {

        val shortFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA)
        val longFormat = SimpleDateFormat("EEEE MMM dd HH:mm:ss Z yyyy", Locale.CANADA)
        val defaultFormat = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.CANADA)

        if (strDate.matches("\\d{4}-\\d{2}-\\d{2}".toRegex())) {
            return parseShortDate(shortFormat, strDate)
        }

        var date: Date? = null
        try {
            date = longFormat.parse(strDate)
        } catch (e: ParseException) {
            // failed, try default format
            date = defaultFormat.parse(strDate)
            try {
                date = defaultFormat.parse(strDate)
            } catch (e: ParseException) {
                // Both formats failed, handle error or log it
            }
        }
        return date
    }
}