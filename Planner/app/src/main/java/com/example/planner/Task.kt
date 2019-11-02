package com.example.planner

import com.example.planner.db.TriggerRule
import java.sql.Time
import java.util.*

class Task(private var date: Date, private var time: Time, private var task: String,
           private var rule : TriggerRule) : Comparable<Task> {

    constructor() : this(Date(), Time .valueOf("11.11.11"), "", TriggerRule.DAILY) {}

    fun getDate() : Date {
        return date;
    }

    fun getTime() : Time {
        return time;
    }

    fun getTask() : String {
        return task;
    }

    fun getRule() : TriggerRule {
        return rule;
    }

    fun setDate(date : Date) {
        this.date = date;
    }

    fun setTime(time : Time) {
        this.time = time;
    }

    fun setTime(time : String) {
        this.time = Time.valueOf(time)
    }

    fun setTask(task : String) {
        this.task = task;
    }

    fun setRule(rule : TriggerRule) {
        this.rule = rule
    }

    override fun compareTo(otherTask : Task): Int {
        if (otherTask.getTime().after(time)) {
            return -1
        }

        return 1
    }
}