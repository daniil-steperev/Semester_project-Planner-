package com.example.planner.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class TriggerService {
    fun readTrigger(mDb : SQLiteDatabase, date : Long) : MutableList<Trigger> {
        val cursor = mDb.rawQuery("SELECT * FROM triggerTable", null)
        val triggerList = mutableListOf<Trigger>()

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val newTrigger = map(cursor)

            if (newTrigger.suits(date)) {
                triggerList.add(newTrigger)
            }

            cursor.moveToNext()
        }

        return triggerList
    }

    private fun map(cursor : Cursor) : Trigger {
        val triggerId = cursor.getString(cursor.getColumnIndex("id")).toLong()

        val triggerRule = when (cursor.getString(cursor.getColumnIndex("rule"))) {
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
