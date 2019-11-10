package com.example.planner.db

import android.database.Cursor
import com.example.planner.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import java.util.*

class EventService {
    //private var listener : Listener = Listener()

    fun addEvent(e : Event, mDb : SQLiteDatabase, dbHelper : DatabaseHelper, triggers : MutableList<Trigger>) {
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

            val listener = Listener()
            listener.addEvent(mDb, e, triggers)

        } catch (e : android.database.sqlite.SQLiteConstraintException){
            println("Such event already exists")
            mDb.endTransaction()
        }

    }

    fun deleteEvent(e : Event, mDb : SQLiteDatabase, dbHelper : DatabaseHelper) {
        val query : String = "DELETE FROM Event WHERE name = \"${e.getName()}\"; "
        mDb.execSQL(query)
    }


    // FIXME: add handling list of triggers!
    fun readEvent(list : MutableList<Trigger>, mDb : SQLiteDatabase) : MutableList<Event> {
        var events : MutableList<Event> = LinkedList<Event>()
        for (i in list) {
            val cursor1 = mDb.rawQuery("SELECT * FROM listener WHERE trigger_id = ${i.getID()}",null)
            cursor1.moveToFirst()
            while (!cursor1.isAfterLast) {
                var event_id = cursor1.getLong(cursor1.getColumnIndex("event_id"))
                val cursor2 = mDb.rawQuery("SELECT * FROM event WHERE id = $event_id", null)
                cursor2.moveToFirst()
                var event : Event = mapEvent(cursor2, mDb)
                println("Event : " + event.getID() + " " + event.getDescription() + "  " + event.getTime() + " " + event.getName())
                events.add(mapEvent(cursor2, mDb))
                cursor1.moveToNext()
                cursor2.close()
            }
            cursor1.close()
        }
        /*cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            events.add(mapEvent(cursor, mDb))
            cursor.moveToNext()
        }
        cursor.close()
        for (i in events) {
            var result : String = "${i.getID()}  ${i.getName()} + ${i.getDescription()} + ${i.getTime()}"
            println(result)
        }*/

        return events
    }

    fun updateEvent(e : Event) {

    }

    private fun mapEvent(cursor : Cursor, mDb: SQLiteDatabase) : Event {
        var e : Event = Event()
        e.setName(cursor.getString(cursor.getColumnIndex("name")))
        e.setID(cursor.getLong(cursor.getColumnIndex("id")))
        /*var result : String = "${e.getID()}  ${e.getName()}"
        println(result)*/
        val query : String = "SELECT * FROM event_param WHERE event_id = ${e.getID()}; "

        var cursorInEventParam : Cursor = mDb.rawQuery(query, null)
        cursorInEventParam.moveToFirst()

        e.setDescription(cursorInEventParam.getString(cursorInEventParam.getColumnIndex("description")))
        e.setTime(cursorInEventParam.getLong(cursorInEventParam.getColumnIndex("time")))

        cursorInEventParam.close()
        /*var result1 : String = "${e.getID()}  ${e.getName()} + ${e.getDescription()} + ${e.getTime()}"
        println(result1)*/
        return e
    }

}