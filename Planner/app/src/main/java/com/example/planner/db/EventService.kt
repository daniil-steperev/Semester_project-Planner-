package com.example.planner.db

import android.content.ContentValues
import android.database.Cursor
import com.example.planner.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import android.graphics.Paint
import java.util.*

class EventService {
    fun addEvent(e : Event, mDb : SQLiteDatabase, dbHelper : DatabaseHelper) {
        try {
            val query1 = "INSERT INTO event (name) VALUES(\"${e.getName()}\"); "
            mDb.beginTransaction()
            mDb.execSQL(query1)
            mDb.setTransactionSuccessful()

            val cursor = mDb.rawQuery("SELECT * FROM event WHERE name = \"${e.getName()}\"", null)
            cursor.moveToFirst()
            var id : Int = cursor.getInt(0)

            val query2 =
                "INSERT INTO event_param (event_id, description, time) VALUES($id, \"${e.getDescription()}\", ${e.getTime()});"
            mDb.execSQL(query2)

            mDb.endTransaction()

        } catch (e : android.database.sqlite.SQLiteConstraintException){
            println("Such event already exists")
        }

    }

    fun deleteEvent(e : Event, mDb : SQLiteDatabase, dbHelper : DatabaseHelper) {
        val query : String = "DELETE FROM Event WHERE name = \"${e.getName()}\"; "
        mDb.execSQL(query)
    }

    fun readEvent(/*list : MutableList<Trigger>,*/ mDb : SQLiteDatabase) : MutableList<Event> {
        val cursor = mDb.rawQuery("SELECT * FROM event",null)
        var events : MutableList<Event> = LinkedList<Event>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            //events.add(mapEvent(cursor, mDb))

            var e : Event = Event()
            e.setName(cursor.getString(cursor.getColumnIndex("name")))
            e.setID(cursor.getLong(cursor.getColumnIndex("id")))
            /*var result : String = "${e.getID()}  ${e.getName()}"
            println(result)*/
            val query : String = "SELECT * FROM event_param WHERE event_id = ${e.getID()}; "

            var cursorInEventParam : Cursor = mDb.rawQuery(query, null)
            cursorInEventParam.moveToFirst()
            e.setDescription(cursorInEventParam.getString(1))
            e.setTime(cursorInEventParam.getLong(2))
            cursorInEventParam.close()
            /*var result1 : String = "${e.getID()}  ${e.getName()} + ${e.getDescription()} + ${e.getTime()}"
            println(result1)*/
            events.add(e)

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