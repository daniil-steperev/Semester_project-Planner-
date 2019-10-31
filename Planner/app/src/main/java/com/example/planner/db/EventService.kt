package com.example.planner.db

import android.content.ContentValues
import android.database.Cursor
import com.example.planner.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import android.graphics.Paint
import java.util.*

class EventService {
    fun addEvent(e : Event, mDb : SQLiteDatabase, dbHelper : DatabaseHelper) {
        val query1 : String = "INSERT INTO Event (name) VALUES(\"${e.getName()}\"); "
        val query2 : String = "INSERT INTO event_param (event_id, description, time) VALUES((SELECT id from Event where name = \"${e.getName()}\"), \"${e.getDescription()}\", \"${e.getTime()}\");"
        //mDb.beginTransaction()
        mDb.execSQL(query1)
        //dbHelper.onUpgrade(mDb, mDb.version, ++mDb.version)
        //dbHelper.updateDataBase()
        //mDb.onUpgrade
        mDb.execSQL(query2)
        //mDb.endTransaction()

    }

    fun deleteEvent(e : Event, mDb : SQLiteDatabase, dbHelper : DatabaseHelper) {
        val query : String = "DELETE FROM Event WHERE name = \"${e.getName()}\"; "
        mDb.execSQL(query)
        dbHelper.updateDataBase()
        //dbHelper.onUpgrade(mDb, mDb.version, ++mDb.version)
    }

    fun readEvent(/*list : MutableList<Trigger>,*/ mDb : SQLiteDatabase) : MutableList<Event> {
        val cursor = mDb.rawQuery("SELECT * FROM Event",null)
        var events : MutableList<Event> = LinkedList<Event>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            //events.add(mapEvent(cursor, mDb))

            var e : Event = Event()
            e.setName(cursor.getString(cursor.getColumnIndex("name")))
            e.setID(cursor.getLong(cursor.getColumnIndex("id")))
            var result : String = "${e.getID()}  ${e.getName()}"
            println(result)
            val query : String = "SELECT * FROM event_param WHERE event_id = ${e.getID()}; "

            var cursorInEventParam : Cursor = mDb.rawQuery(query, null)
            cursorInEventParam.moveToFirst()
            println(cursorInEventParam.getString(2))
            println(cursorInEventParam.getLong(3))
            //e.setDescription(cursorInEventParam.getString(2))
            //e.setTime(cursorInEventParam.getLong(3))
            //events.add(e)

            cursor.moveToNext()
        }
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