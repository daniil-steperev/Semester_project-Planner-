package com.example.planner.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.planner.DatabaseHelper
import java.util.*

class EventService {

    fun addEvent(e: Event, mDb: SQLiteDatabase, triggers: MutableList<TriggerRule>) {
        val query1 = "INSERT INTO event (name) VALUES(\"${e.getName()}\"); "

        mDb.beginTransaction()
        try {
            mDb.execSQL(query1)

            val cursor = mDb.rawQuery("SELECT * FROM event WHERE name = \"${e.getName()}\"", null)
            var id : Int = -1
            cursor.use { cursor ->
                cursor.moveToFirst()
                id = cursor.getInt(0)
            }
            if (id == -1) {
                return
            }
            val query2 =
                "INSERT INTO event_param (event_id, description, time) VALUES($id, \"${e.getDescription()}\", ${e.getTime()});"
            mDb.execSQL(query2)

            mDb.setTransactionSuccessful()
        } catch (e : android.database.sqlite.SQLiteConstraintException) {
            println("Such event already exists")
        } finally {
            mDb.endTransaction()
        }

        val listener = Listener()
        listener.addEvent(mDb, e, triggers)

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

        var events = mutableListOf<Event>()

        val queryEventId = "SELECT * FROM event_param WHERE (time > $startTime) AND (time < $endTime)"
        val cursor = mDb.rawQuery(queryEventId, null)

        cursor.use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val event = Event()

                event.setTime(cursor.getLong(cursor.getColumnIndex("time")))
                event.setDescription(cursor.getString(cursor.getColumnIndex("description")))
                event.setID(cursor.getLong(cursor.getColumnIndex("event_id")))

                val eventId = cursor.getLong(cursor.getColumnIndex("event_id"))
                val queryEvent = "SELECT * FROM event WHERE id = $eventId"

                val eventCursor = mDb.rawQuery(queryEvent, null)
                eventCursor.use { eventCursor ->
                    eventCursor.moveToFirst()
                    event.setName(eventCursor.getString(eventCursor.getColumnIndex("name")))
                    events.add(event)

                    eventCursor.close()
                }
                cursor.moveToNext()
            }

            cursor.close()
        }

        return events
    }

    fun deleteEvent(e: Event, mDb: SQLiteDatabase) {
        mDb.beginTransaction()
        try {
            val cursor1 = mDb.rawQuery("SELECT * FROM event WHERE name = \"${e.getName()}\"; ", null)
            cursor1.use { cursor1 ->
                cursor1.moveToFirst()
                val id: Long = cursor1.getLong(cursor1.getColumnIndex("id"))

                mDb.delete("event_param", "event_id = \"$id\"", null)

                val cursor2 = mDb.rawQuery("SELECT * FROM listener WHERE event_id = $id", null)

                cursor2.use { cursor2 ->
                    cursor2.moveToFirst()
                    while (!cursor2.isAfterLast) {
                        mDb.delete(
                            "listener",
                            "id = \"${cursor2.getLong(cursor2.getColumnIndex("id"))}\"",
                            null
                        )
                        cursor2.moveToNext()
                    }

                    mDb.delete("event", "name = \"${e.getName()}\"", null)
                    cursor2.close()
                }

                cursor1.close()
            }

            mDb.setTransactionSuccessful()
        } catch (e : Exception) {
            println("Such object does not exist")
        } finally {
            mDb.endTransaction()
        }
    }

    fun readEvent(list : MutableList<Trigger>, mDb : SQLiteDatabase) : MutableList<Event> {
        var events : MutableList<Event> = LinkedList()
        for (i in list) {
            val cursor1 = mDb.rawQuery("SELECT * FROM listener WHERE trigger_id = ${i.getID()}",null)
            cursor1.use { cursor1 ->
                cursor1.moveToFirst()
                while (!cursor1.isAfterLast) {
                    var event_id = cursor1.getLong(cursor1.getColumnIndex("event_id"))
                    val cursor2 = mDb.rawQuery("SELECT * FROM event WHERE id = $event_id", null)
                    cursor2.use { cursor2 ->
                        cursor2.moveToFirst()
                        events.add(mapEvent(cursor2, mDb))
                        cursor1.moveToNext()
                        cursor2.close()
                    }
                }
                cursor1.close()
            }
        }

        return events
    }

    private fun mapEvent(cursor : Cursor, mDb: SQLiteDatabase) : Event {
        var e : Event = Event()
        e.setName(cursor.getString(cursor.getColumnIndex("name")))
        e.setID(cursor.getLong(cursor.getColumnIndex("id")))

        val query : String = "SELECT * FROM event_param WHERE event_id = ${e.getID()}; "

        var cursorInEventParam = mDb.rawQuery(query, null)
        cursorInEventParam.use {
            cursorInEventParam.moveToFirst()

            e.setDescription(cursorInEventParam.getString(cursorInEventParam.getColumnIndex("description")))
            e.setTime(cursorInEventParam.getLong(cursorInEventParam.getColumnIndex("time")))

            cursorInEventParam.close()
        }

        return e
    }

}
