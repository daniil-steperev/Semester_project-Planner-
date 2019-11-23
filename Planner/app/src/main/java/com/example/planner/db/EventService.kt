package com.example.planner.db

import android.database.Cursor
import com.example.planner.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import java.util.*

class EventService {

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
            cursor.close()
        } catch (e : android.database.sqlite.SQLiteConstraintException){
            println("Such event already exists")
            mDb.endTransaction()
        }

    }

    fun deleteEvent(e : Event, mDb : SQLiteDatabase, dbHelper : DatabaseHelper) {
        try {
        mDb.beginTransaction()

        val cursor1 = mDb.rawQuery("SELECT * FROM event WHERE name = \"${e.getName()}\"; ", null)
        cursor1.moveToFirst()
        val id : Long = cursor1.getLong(cursor1.getColumnIndex("id"))

        mDb.delete("event_param", "event_id = \"$id\"", null)

        val cursor2 = mDb.rawQuery("SELECT * FROM listener WHERE event_id = $id", null)
        cursor2.moveToFirst()
        while (!cursor2.isAfterLast) {
            mDb.delete("listener", "id = \"${cursor2.getLong(cursor2.getColumnIndex("id"))}\"", null)
            cursor2.moveToNext()
        }

        mDb.delete("event", "name = \"${e.getName()}\"", null)

        cursor1.close()
        cursor2.close()

        mDb.setTransactionSuccessful()
        mDb.endTransaction()
        } catch (e : Exception) {
            println("Such object already exists")
            mDb.endTransaction()
        }

        //debugging print
        /*val cursorDebug2 = mDb.rawQuery("SELECT * FROM event_param", null)
        cursorDebug2.moveToFirst()
        while (!cursorDebug2.isAfterLast) {
            println(cursorDebug2.getString(cursorDebug2.getColumnIndex("event_id"))
                    + " " + cursorDebug2.getString(cursorDebug2.getColumnIndex("description")) + " "
                    + cursorDebug2.getLong(cursorDebug2.getColumnIndex("time")))
            cursorDebug2.moveToNext()
        }
        cursorDebug2.close()
        val cursorDebug3 = mDb.rawQuery("SELECT * FROM listener", null)
        cursorDebug3.moveToFirst()
        while (!cursorDebug3.isAfterLast) {
            println("DFGDFGDFG" + cursorDebug3.getLong(cursorDebug3.getColumnIndex("event_id")) +
                    " " + cursorDebug3.getLong(cursorDebug3.getColumnIndex("trigger_id")) + " "
                    + cursorDebug3.getLong(cursorDebug3.getColumnIndex("id")))
            cursorDebug3.moveToNext()
        }
        cursorDebug3.close()

        val cursorDebug1 = mDb.rawQuery("SELECT * FROM event", null)
        cursorDebug1.moveToFirst()
        while (!cursorDebug1.isAfterLast) {
            println(cursorDebug1.getString(cursorDebug1.getColumnIndex("name")))
            cursorDebug1.moveToNext()
        }
        cursorDebug1.close()*/
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
                //println("Event : " + event.getID() + " " + event.getDescription() + "  " + event.getTime() + " " + event.getName())
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

    /*fun returnAllEventsInGivenPeriodOfTime(start : Long, end : Long, mDb : SQLiteDatabase) : MutableList<Event> {
        val cursor = mDb.rawQuery("SELECT * From event_param WHERE (time > $start AND time < $end);",null)
        var events : MutableList<Event> = LinkedList<Event>()

        while (!cursor.isAfterLast) {
            var event_id = cursor.getLong(cursor.getColumnIndex("event_id"))

            val cursor2 = mDb.rawQuery("SELECT * FROM event WHERE id = $event_id", null)
            cursor2.moveToFirst()
            var event : Event = mapEvent(cursor2, mDb)

            println("Event : " + event.getID() + " " + event.getDescription() + "  " + event.getTime() + " " + event.getName())

            events.add(mapEvent(cursor2, mDb))
            cursor.moveToNext()
            cursor2.close()
        }
        cursor.close()
        return events
    }*/

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
