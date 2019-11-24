package com.example.planner.db

import android.database.sqlite.SQLiteDatabase

class Listener {
    fun addEvent(mDb : SQLiteDatabase, event : Event, triggers : MutableList<TriggerRule>) {
        try {
            var cursor = mDb.rawQuery("SELECT * FROM event WHERE name = \"${event.getName()}\"", null)
            cursor.moveToFirst()
            val eventID = cursor.getInt(0)

            mDb.beginTransaction()
            for (triggerRule in triggers) {
                cursor = mDb.rawQuery("SELECT * FROM triggerTable WHERE rule = \"${triggerRule}\"", null)
                cursor.moveToFirst()
                val triggerID = cursor.getInt(0)

                val insertQuery = "INSERT INTO listener (trigger_id, event_id, type) VALUES($triggerID, $eventID, 'type')"
                mDb.execSQL(insertQuery)
            }

            mDb.setTransactionSuccessful()
            mDb.endTransaction()

            cursor.close()

            val cursor1 = mDb.rawQuery("SELECT * FROM listener", null)
            println("PRINT listener table")
            cursor1.moveToFirst()
            while (!cursor1.isAfterLast) {
                println("id" + cursor1.getLong(cursor1.getColumnIndex("id")) + "event_id" + cursor1.getLong(cursor1.getColumnIndex("event_id")) + "trigger_id" + cursor1.getLong(cursor1.getColumnIndex("trigger_id")))
                cursor1.moveToNext()
            }
            cursor1.close()

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
            cursor.moveToNext()
        }
        cursor.close()
    }
}