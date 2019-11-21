package com.example.planner.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.util.*

class JournalEntryService() {
    private var eventService : EventService = EventService()
    private var lastEntryDate : Long = 0

    fun addPassedEvents(mDb : SQLiteDatabase) {
        var currentTime = System.currentTimeMillis()
        var eventsToAdd  = eventService.returnAllEventsInGivenPeriodOfTime(lastEntryDate, currentTime, mDb)
        for (i in eventsToAdd) {
            val query1 = "INSERT INTO event_journal (name, time, description, successful) " +
                    "VALUES(\"${i.getName()}\", \"${i.getTime()}, \"${i.getDescription()}\", 1); "
            mDb.beginTransaction()
            mDb.execSQL(query1)
            mDb.setTransactionSuccessful()
            mDb.endTransaction()
        }
        lastEntryDate = eventsToAdd.get(eventsToAdd.size).getTime()
        // FIXME СКАЗАТЬ ДАНЕ, ЧТОБЫ ОН ВСЕГДА ИНИЦИАЛИЗИРОВАЛ ВРЕМЯ КОНЦА СОБЫТИЯ, ДАЖЕ ЕСЛИ ОНО НЕ УКАЗАНО
        // FIXME SUCCESSFUL MUST BE NOT NULL
    }

    fun returnEntriesForGivenPeriodOfTime(start : Long, end : Long, mDb : SQLiteDatabase) : MutableList<JournalEntry> {
        val cursor = mDb.rawQuery("SELECT * FROM event_journal WHERE (time > $start AND time < $end);",null)
        var entries : MutableList<JournalEntry> = LinkedList()
        while (!cursor.isAfterLast) {
            entries.add(mapEntry(cursor, mDb))
        }
        return entries
    }

    fun mapEntry(cursor : Cursor, mDb : SQLiteDatabase) : JournalEntry {
        var e : JournalEntry = JournalEntry()
        e.setName(cursor.getString(cursor.getColumnIndex("name")))
        e.setID(cursor.getLong(cursor.getColumnIndex("id")))
        e.setDescription(cursor.getString(cursor.getColumnIndex("description")))
        e.setTime(cursor.getLong(cursor.getColumnIndex("time")))
        e.setDoneSuccessful(cursor.getInt(cursor.getColumnIndex("successful")))
        cursor.close()
        /*var result1 : String = "${e.getID()}  ${e.getName()} + ${e.getDescription()} + ${e.getTime()}"
        println(result1)*/
        return e
    }
}