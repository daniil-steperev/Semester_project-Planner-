package com.example.planner.db

import android.database.Cursor

class TriggerService {
    // FIXME: SELECT OFFER (запрос)

    fun readTrigger(date : Long) : MutableList<Trigger> {
        // FIXME
        return mutableListOf()
    }

    fun map(cursor : Cursor) : Trigger {
        val triggerId = cursor.getString(cursor.getColumnIndex("id")).toLong()
        val rule = cursor.getString(cursor.getColumnIndex("rule"))

        val triggerRule = when (rule) {
            "MONDAY" -> TriggerRule.MONDAY
            "TUESDAY" -> TriggerRule.TUESDAY
            "WEDNESDAY" -> TriggerRule.WEDNESDAY
            "THURSDAY" -> TriggerRule.THURSDAY
            "FRIDAY" -> TriggerRule.FRIDAY
            "SATURDAY" -> TriggerRule.SATURDAY
            "SUNDAY" -> TriggerRule.SUNDAY
            "DAILY" -> TriggerRule.DAILY
            "WEEKLY" -> TriggerRule.WEEKLY
            else -> TriggerRule.DAILY // FIXME: maybe call an exception?
        }

        return Trigger(triggerId, triggerRule)
    }
}