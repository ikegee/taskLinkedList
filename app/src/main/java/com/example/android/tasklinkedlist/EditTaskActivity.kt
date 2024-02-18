package com.example.android.tasklinkedlist

import android.content.ActivityNotFoundException
import android.content.Intent
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
import com.example.android.tasklinkedlist.DateUtils.Companion.formatDate
import com.example.tasklinkedlist.R
import java.util.UUID

/**
 * Project: TaskLinkedList File: EditTaskActivity.java
 * Created by G.E. Eidsness Date: Feb. 15, 2017 Time: 10:34:43 PM
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
        }
        // same as AddTaskActivity
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

    // Modify task object with the data from the EditTaskActivity
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
        finish()
        // Start new DisplayTasksActivity instance
        val intent = Intent(applicationContext, DisplayTasksActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setTaskStatus(taskCheckBox: CheckBox?, `var`: Boolean) {
        if (`var`) {
            taskCheckBox!!.setText(R.string.task_checked_completed)
        } else taskCheckBox!!.setText(   R.string.task_checked_pending)
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
        val TAG: String? = EditTaskActivity::class.java.simpleName
        private const val DIALOG_DATE = "DialogDate"
        private val taskInstance = TaskList.getInstance()
        private const val EMAILADDRESS = "joeblow@from.idaho.ca"
        private var EMAILSENT = false
        private var theCategory: String? = null
        private var thePriority: String? = null
        private var isChecked: Boolean = false
    }
}