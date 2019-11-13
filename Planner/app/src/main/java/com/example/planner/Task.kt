package com.example.planner

import com.example.planner.db.TriggerRule
import java.sql.Time
import java.util.*

class Task(private var date: Date, private var time: Time, private var task: String,
           private var rule : MutableList<TriggerRule>) : Comparable<Task> {

    constructor() : this(Date(), Time .valueOf("11:11:11"), "", mutableListOf()) {}

    fun getDate() : Date {
        return date;
    }

    fun getTime() : Time {
        return time;
    }

    fun getTask() : String {
        return task;
    }

    fun getRule() : MutableList<TriggerRule> {
        return rule;
    }

    fun setDate(date : Date) {
        this.date = date;
    }

    fun setTime(time : Time) {
        this.time = time;
    }

    fun setTime(time : String) {
        println("PROOOOOOBLEM IS TIIIIIIME!!!!!")
        this.time = Time.valueOf(time)
    }

    fun setTask(task : String) {
        this.task = task;
    }

    fun addRule(rule : TriggerRule) {
        this.rule.add(rule)
    }

    fun removeRule(rule : TriggerRule) {
        this.rule.remove(rule)
    }

    fun isRuleContains(newRule : TriggerRule) : Boolean {
        return rule.contains(newRule)
    }

    override fun compareTo(otherTask : Task): Int {
        if (otherTask.getTime().after(time)) {
            return -1
        }

        return 1
    }
}
