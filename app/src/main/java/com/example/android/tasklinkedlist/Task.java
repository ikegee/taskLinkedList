package com.example.android.tasklinkedlist;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Author: G.E. Eidsness
 * Project: TaskLinkedList File: Task.java
 * Created:  2017-08-15; Updated : 2023-12-08
 * Modified: 2024-02-06
 */

public class Task {

    private final int mpId;
    private final UUID mId;
    private Date mDate;
    private String mTitle;
    private String mDescription;
    private boolean mTasked;
    private String mCategory;
    private String mPriority;

    Task() {
        mpId = (int) Math.floor((Math.random() * 100000) + 1);
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    @NonNull
    public UUID getId() {
        return mId;
    }

    Date getDate() {
        return mDate;
    }

    void setDate(Date date) {
        mDate = date;
    }

    String getTitle() {
        return mTitle;
    }

    void setTitle(String title) {
        mTitle = title;
    }

    String getDescription() {
        return mDescription;
    }

    void setDescription(String Description) {
        mDescription = Description;
    }

    boolean isTasked() {
        return mTasked;
    }

    void setTasked(boolean solved) {
        mTasked = solved;
    }

    String getPriority() {
        return mPriority;
    }

    void setPriority(String Priority) {
        mPriority = Priority;
    }

    String getCategory() {
        return mCategory;
    }

    void setCategory(String Category) {
        mCategory = Category;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(
                "%s [mpId=%s, mId=%s, mDate=%s, mTitle=%s, mDescription=%s, mTasked=%s, mCategory=%s, mPriority=%s]",
                getClass().getSimpleName(), mpId, mId, mDate, mTitle, mDescription, mTasked, mCategory, mPriority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mpId, mId, mDate, mTitle, mDescription, mTasked, mCategory, mPriority);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Task other = (Task) obj;
        return mpId == other.mpId &&
                Objects.equals(mId, other.mId) &&
                Objects.equals(mDate, other.mDate) &&
                Objects.equals(mTitle, other.mTitle) &&
                Objects.equals(mDescription, other.mDescription) &&
                mTasked == other.mTasked &&
                Objects.equals(mCategory, other.mCategory) &&
                Objects.equals(mPriority, other.mPriority);
    }
}

