package com.example.planner.db

import android.database.sqlite.SQLiteDatabase

class Listener {
    fun addEvent(mDb : SQLiteDatabase, event : Event, triggers : MutableList<TriggerRule>) {
        var cursor = mDb.rawQuery("SELECT * FROM event WHERE name = \"${event.getName()}\"", null)
        try {
            cursor.moveToFirst()
            val eventID = cursor.getInt(0)

            mDb.beginTransaction()
            try {
                for (triggerRule in triggers) {
                    cursor = mDb.rawQuery(
                        "SELECT * FROM triggerTable WHERE rule = \"${triggerRule}\"",
                        null
                    )
                    cursor.moveToFirst()
                    val triggerID = cursor.getInt(0)

                    val insertQuery =
                        "INSERT INTO listener (trigger_id, event_id, type) VALUES($triggerID, $eventID, 'type')"
                    mDb.execSQL(insertQuery)
                }

                mDb.setTransactionSuccessful()
            } finally {
                mDb.endTransaction()
            }
            cursor.close()
        } finally {
            cursor.close()
        }
    }
}