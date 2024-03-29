package com.example.android.tasklinkedlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tasklinkedlist.R;

import java.util.LinkedList;

/**
 * Project: TaskLinkedList File: TaskListAdapter.java
 * Created by G.E. Eidsness on 2017-08-15.
 * Updated: 2023-12-08
 */

class TaskListAdapter extends ArrayAdapter<Task> {

    private static final String TAG = TaskListAdapter.class.getSimpleName();
    private final Context context;
    private final LinkedList<Task> mTasks;

    TaskListAdapter(Context ctx, LinkedList<Task> tasks) {
        super(ctx, R.layout.list_task_items, tasks);
        context = ctx;
        mTasks = tasks;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View rowView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_task_items, parent, false);
        } else
            rowView = convertView;

        Task task = mTasks.get(position);

        TextView titleTextView = rowView.findViewById(R.id.task_list_item_titleTextView);
        titleTextView.setText(task.getTitle());
        TextView descriptionTextView = rowView.findViewById(R.id.task_list_item_descriptionTextView);
        descriptionTextView.setText(task.getDescription());
        TextView dateTextView = rowView.findViewById(R.id.task_list_item_dateTextView);
        if (task.getDate() != null) {
            dateTextView.setText(task.getDate().toString());
        } else {
            dateTextView.setText("Null");
        }
        TextView categoryTextView = rowView.findViewById(R.id.task_list_item_categoryTextView);
        categoryTextView.setText(String.format("Category: %s", task.getCategory()));
        TextView priorityTextView = rowView.findViewById(R.id.task_list_item_priorityTextView);
        priorityTextView.setText(String.format("Priority: %s", task.getPriority()));
        CheckBox statusCheckBox = rowView.findViewById(R.id.task_list_item_taskCheckBox);
        statusCheckBox.setChecked(task.isTasked());
        ImageView icon = rowView.findViewById(R.id.task_list_item_iconView);
        setPriorityIcon(task, icon);
        return rowView;
    }

    private void setPriorityIcon(Task task, ImageView icon) {
        switch (task.getPriority()) {
            case "Low":
                icon.setImageResource(R.drawable.ic_error_black_24dp);
                break;
            case "Medium":
                icon.setImageResource(R.drawable.ic_error_outline_black_24dp);
                break;
            case "High":
                icon.setImageResource(R.drawable.ic_warning_black_24dp);
                break;
            default:
                Log.d(TAG, "default");
                icon.setImageResource(R.drawable.ic_priority_high);
        }
        Log.d(TAG, task.getPriority());
    }
}

