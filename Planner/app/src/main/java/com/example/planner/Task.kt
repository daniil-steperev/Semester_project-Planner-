package com.example.planner

import java.sql.Time
import java.util.*

class Task(
    private var date: Date,
    private var time: Time,
    private var task: String
) {
    fun getDate() : Date {
        return date;
    }

    fun getTime() : Time {
        return time;
    }

    fun getTask() : String {
        return task;
    }

    fun setDate(date : Date) {
        this.date = date;
    }

    fun setTime(time : Time) {
        this.time = time;
    }

    fun setTask(task : String) {
        this.task = task;
    }
}