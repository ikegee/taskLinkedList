package com.example.android.tasklinkedlist;

import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Project: TaskLinkedList File: TaskList.java
 * Created by G.E. Eidsness on 2017-08-15
 * Modified: 2023-12-08
 */

public class TaskList {

    private static final String TAG = TaskList.class.getSimpleName();
    private final LinkedList<Task> tasks  = new LinkedList<>();

    private TaskList() {
        super();
    }

    private static final class STaskListHolder {
        static final TaskList sTaskList = new TaskList();
    }

    static TaskList getInstance() {
        return STaskListHolder.sTaskList;
    }

    LinkedList<Task> getTasks() {
        return tasks;
    }

    Task getTask(UUID id) {
        if (id != null) {
            for (Task task : tasks) {
                if (task.getId().equals(id)) {
                    return task;
                }
            }
        }
        return null;
    }

    void addTask(Task task) {
        boolean isAdded = false;
        if (task != null) {
            synchronized (this) {
                tasks.add(task);
                isAdded = true;
            }
        }
        Log.d(TAG, "Created: " + isAdded);
    }

    boolean deleteTaskWithId(UUID id) {
        boolean isDeleted = false;
        if (id != null) {
            for (Task task : tasks) {
                if (task.getId().equals(id)) {
                    tasks.removeAll(Collections.singleton(task));
                    isDeleted = true;
                    break;
                }
            }
        }
        Log.d(TAG, "Deleted: " + isDeleted);
        return isDeleted;
    }

    boolean updateTask(Task editTask) {
        boolean isUpdated = false;
        int j = 0;
        if (editTask != null) {
            for (Task task : tasks) {
                if (editTask.equals(task)) {
                    Log.d(TAG, "index:" + j);
                    tasks.set(j, editTask);
                    isUpdated = true;
                    break;
                }
                j++;
            }
        }
        Log.d(TAG, "Updated: " + isUpdated);
        return isUpdated;
    }

}

