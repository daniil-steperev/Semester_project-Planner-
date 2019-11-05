package com.example.planner.db

import java.sql.Time
import java.time.LocalDateTime
import java.util.*

class Trigger {
    private var id : Long = 0
    private var rule : TriggerRule

    constructor(id : Long, rule : TriggerRule) {
        this.id = id
        this.rule = rule
    }

    fun getRule() : TriggerRule {
        return rule
    }

    fun suits(date : Long) : Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // get current day of week in int
        // 1 = SUNDAY, 2 = MONDAY, 3 = TUESDAY, 4 = WEDNESDAY, 5 = THURSDAY, 6 = FRIDAY,
        // 7 = SATURDAY

        when (rule) {
            TriggerRule.DAILY -> {
                return true
            }

            TriggerRule.MONDAY -> {
                return dayOfWeek == 2
            }

            TriggerRule.TUESDAY -> {
                return dayOfWeek == 3
            }

            TriggerRule.WEDNESDAY -> {
                return dayOfWeek == 4
            }

            TriggerRule.THURSDAY -> {
                return dayOfWeek == 5
            }

            TriggerRule.FRIDAY -> {
                return dayOfWeek == 6
            }

            TriggerRule.SATURDAY -> {
                return dayOfWeek == 7
            }

            TriggerRule.SUNDAY -> {
                return dayOfWeek == 1
            }
        }

        return false
    }
}