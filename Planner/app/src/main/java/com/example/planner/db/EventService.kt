package com.example.planner.db

import android.content.ContentValues
import android.database.Cursor
import com.example.planner.DatabaseHelper
import android.database.sqlite.SQLiteDatabase

class EventService {
    fun addEvent(e : Event, mDb : SQLiteDatabase) {
        val query : String = "INSERT INTO Event (name) VALUES(\"${e.getName()}\"); " +
                "INSERT INTO event_param (event_id, description, time) " +
                "VALUES((SELECT id from Event where name = \"${e.getName()}\"), \"${e.getDescription()}\", \"${e.getTime()}\");"
        mDb.execSQL(query)
    }

    fun deleteEvent(e : Event) {

    }

    /*fun readEvent(list : MutableList<Trigger>) : {

    }*/

    fun updateEvent(e : Event) {

    }

    /*fun map(cursor : Cursor) : Event {

    }*/
}