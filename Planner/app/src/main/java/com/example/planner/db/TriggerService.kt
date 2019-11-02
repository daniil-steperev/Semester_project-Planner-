package com.example.planner.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class TriggerService {
    private lateinit var mDb : SQLiteDatabase
    // FIXME: SELECT OFFER (запрос)

    fun readTrigger(date : Long) : MutableList<Trigger> {
        // FIXME
        val cursor = mDb.rawQuery("SELECT * FROM trigger ", null) // FIXME
        val triggerList = mutableListOf<Trigger>()

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val newTrigger = map(cursor)

            if (newTrigger.suits(date)) {
                triggerList.add(newTrigger)
            }
        }
        return triggerList
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

    fun addTrigger(newTrigger : Trigger) {

    }
}