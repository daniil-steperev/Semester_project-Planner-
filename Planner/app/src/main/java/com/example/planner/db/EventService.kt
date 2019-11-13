package com.example.planner.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.sqlite.transaction
import java.lang.IndexOutOfBoundsException
import java.util.*

class EventService {
    //private var listener : Listener = Listener()

    fun addEvent(
        e: Event,
        mDb: SQLiteDatabase,
        triggers: MutableList<TriggerRule>
    ) {
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

    fun getAllEventsForToday(mDb : SQLiteDatabase, originalCalendar : Calendar) : MutableList<Event> {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = originalCalendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val startTime = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 59)

        val endTime = calendar.timeInMillis
        println("START TIME = $startTime    END TIME = $endTime")

        var events = mutableListOf<Event>()

        val queryEventId = "SELECT * FROM event_param WHERE (time > $startTime) AND (time < $endTime)"
        val cursor = mDb.rawQuery(queryEventId, null)
        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            val event = Event()

            event.setTime(cursor.getLong(cursor.getColumnIndex("time")))
            event.setDescription(cursor.getString(cursor.getColumnIndex("description")))
            event.setID(cursor.getLong(cursor.getColumnIndex("event_id")))

            val eventId = cursor.getLong(cursor.getColumnIndex("event_id"))
            val queryEvent = "SELECT * FROM event WHERE id = $eventId"

            val eventCursor = mDb.rawQuery(queryEvent, null)
            eventCursor.moveToFirst()
            event.setName(eventCursor.getString(eventCursor.getColumnIndex("name")))

            events.add(event)
            eventCursor.close()
            cursor.moveToNext()
        }

        cursor.close()

        return events
    }

    fun deleteEvent(e: Event, mDb: SQLiteDatabase) {
        val query = "DELETE FROM Event WHERE name = \"${e.getName()}\"; "
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
