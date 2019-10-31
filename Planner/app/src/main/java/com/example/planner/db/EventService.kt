package com.example.planner.db

import android.content.ContentValues
import android.database.Cursor
import com.example.planner.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import android.graphics.Paint
import java.util.*

class EventService {
    fun addEvent(e : Event, mDb : SQLiteDatabase) {
        val query : String = "INSERT INTO Event (name) VALUES(\"${e.getName()}\"); " +
                "INSERT INTO event_param (event_id, description, time) " +
                "VALUES((SELECT id from Event where name = \"${e.getName()}\"), \"${e.getDescription()}\", \"${e.getTime()}\");"
        mDb.execSQL(query)
    }

    fun deleteEvent(e : Event, mDb : SQLiteDatabase) {
        val query : String = "DELETE FROM Event WHERE name = \"${e.getName()}\"; "
        mDb.execSQL(query)
    }

    fun readEvent(/*list : MutableList<Trigger>,*/ mDb : SQLiteDatabase) : MutableList<Event> {
        val cursor = mDb.rawQuery("SELECT * FROM Event",null)
        var events : MutableList<Event> = LinkedList<Event>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var e : Event = Event()
                    e.setName(cursor.getString(cursor.getColumnIndex("name")))
                    e.setID(cursor.getLong(cursor.getColumnIndex("id")))

                    val query : String = "SELECT * FROM event_param WHERE event_id = ${e.getID()}; "

                    var cursorInEventParam : Cursor = mDb.rawQuery(query, null)

                    e.setDescription(cursorInEventParam.getString(cursorInEventParam.getColumnIndex("description")))
                    e.setTime(cursorInEventParam.getLong(cursorInEventParam.getColumnIndex("time")))
                    events.add(e)
                } while (cursor.moveToNext())
            }
        }
            //events.add(mapEvent(cursor, mDb))
        cursor.close()
        for (i in events) {
            var result : String = "${i.getID()}  ${i.getName()} + ${i.getDescription()} + ${i.getTime()}"
            println(result)
        }
        return events
    }

    fun updateEvent(e : Event) {

    }

    private fun mapEvent(cursor : Cursor, mDb: SQLiteDatabase) : Event {
        var e : Event = Event()
        e.setName(cursor.getString(cursor.getColumnIndex("name")))
        e.setID(cursor.getLong(cursor.getColumnIndex("id")))

        val query : String = "SELECT * FROM event_param WHERE event_id = ${e.getID()}; "

        var cursorInEventParam : Cursor = mDb.rawQuery(query, null)

        e.setDescription(cursorInEventParam.getString(cursorInEventParam.getColumnIndex("description")))
        e.setTime(cursorInEventParam.getLong(cursorInEventParam.getColumnIndex("time")))

        return e
    }

}