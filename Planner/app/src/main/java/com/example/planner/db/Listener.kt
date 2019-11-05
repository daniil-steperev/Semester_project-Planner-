package com.example.planner.db

import android.database.sqlite.SQLiteDatabase

class Listener {
    fun addEvent(mDb : SQLiteDatabase, event : Event, triggers : MutableList<Trigger>) {
        try {
            var cursor = mDb.rawQuery("SELECT * FROM event WHERE name = \"${event.getName()}\"", null)
            cursor.moveToFirst()
            val eventID = cursor.getInt(0)

            mDb.beginTransaction()
            for (trigger in triggers) {
                cursor = mDb.rawQuery("SELECT * FROM triggerTable WHERE rule = \"${trigger.getRule()}\"", null)
                cursor.moveToFirst()
                val triggerID = cursor.getInt(0)

                val insertQuery = "INSERT INTO listener (trigger_id, event_id, type) VALUES($triggerID, $eventID, 'type')"
                mDb.execSQL(insertQuery)
            }

            mDb.endTransaction()
        } catch (e : android.database.sqlite.SQLiteConstraintException) {
            println("Can't add to listener table.")
            mDb.endTransaction()
        }
    }

    //FIXME
    fun readAddedEvents(mDb : SQLiteDatabase) {
        val cursor = mDb.rawQuery("SELECT * FROM listener", null)

        println("IN LISTENER")
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            println(cursor.getString(cursor.getColumnIndex("trigger_id")))
        }
    }
}