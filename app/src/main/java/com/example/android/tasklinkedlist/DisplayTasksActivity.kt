package com.example.android.tasklinkedlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tasklinkedlist.R
import java.util.LinkedList

/**
 * Project: TaskLinkedList File: DisplayTasksActivity.java
 * Created by G.E. Eidsness on 2017-08-15
 * Updated: 2023-12-09 => DisplayTasksActivity.kt
 * Modified: 2024-02-06
 */
class DisplayTasksActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var mTasks: LinkedList<Task>? = null
    private val taskList = TaskList.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_tasks)
        val taskText: TextView = findViewById(R.id.taskListTitle)
        val lvTasks = findViewById<ListView>(R.id.taskListView)

        mTasks = taskList.tasks
        if (mTasks == null || mTasks!!.isEmpty()) {
            taskText.setText(R.string.taskListEmpty)
            taskText.setOnClickListener {
                val intent = Intent(this.applicationContext, AddTaskActivity::class.java)
                startActivity(intent)
            }
        } else { taskText.setText(R.string.display_activity_name) }

        Log.d(TAG, "getTasks()" + mTasks.toString())
        val taskListAdapter = TaskListAdapter(this, mTasks)
        lvTasks.adapter = taskListAdapter
        lvTasks.isTextFilterEnabled = true
        lvTasks.onItemClickListener = this
    }

    override fun onItemClick(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {
        val editIntent = Intent(applicationContext, EditTaskActivity::class.java)
        editIntent.putExtra("taskID", mTasks!![position].id)
        editIntent.putExtra("taskTitle", mTasks!![position].title)
        editIntent.putExtra("taskDescription", mTasks!![position].description)
        editIntent.putExtra("taskDate", mTasks!![position].date.toString())
        editIntent.putExtra("taskStatus", mTasks!![position].isTasked)
        editIntent.putExtra("taskCategory", mTasks!![position].category)
        editIntent.putExtra("taskPriority", mTasks!![position].priority)
        editIntent.putExtra("taskPosition", position)
        startActivity(editIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                val intent = Intent(applicationContext, AddTaskActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val TAG = DisplayTasksActivity::class.java.simpleName
    }
}
