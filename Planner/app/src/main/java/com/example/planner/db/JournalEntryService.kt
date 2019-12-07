package com.example.planner.db

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.graphics.toColorLong
import java.util.*

class JournalEntryService {

    private var lastEntryDate : Long = 0

    fun computateInitDate() : Long {
        var minDate= Calendar.getInstance()
        minDate.add(Calendar.YEAR, -1)
        minDate.set(Calendar.DAY_OF_MONTH, 1)
        return minDate.timeInMillis
    }


    fun setLastEntryDate(date : Long) {
        println("WE ARE IN SETTER")
        if (date == 0.toLong()) {
            println("COMPUTATEINITDATE")
            lastEntryDate = computateInitDate()
            println("NOW DATE IS" + lastEntryDate)
            return
        }
        lastEntryDate = date
    }

    fun getLastEntryDate() : Long {
        return lastEntryDate
    }

    fun addPassedEvents(mDb : SQLiteDatabase, eventService : EventService, triggerService: TriggerService) {

        var currentTime = System.currentTimeMillis()

        println("dates now is " + lastEntryDate + " " + currentTime)
        //var eventsToAdd  = eventService.returnAllEventsInGivenPeriodOfTime(lastEntryDate, currentTime, mDb)
        while (lastEntryDate < currentTime) {
            var triggers : MutableList<Trigger> = triggerService.readTrigger(mDb, lastEntryDate)
            var list : List<Event> =  eventService.readEvent(triggers, mDb)
            for (i in list) {
                val query1 = "INSERT INTO event_journal (name, time, description, successful) " +
                        "VALUES(\"${i.getName()}\", $lastEntryDate, \"${i.getDescription()}\", 1); "
                mDb.beginTransaction()
                try {
                    mDb.execSQL(query1)
                    mDb.setTransactionSuccessful()
                } finally {
                    mDb.endTransaction()
                }
            }
            lastEntryDate += 86400000
            println("HAHA")
        }

        /*val cursorDebug2 = mDb.rawQuery("SELECT * FROM event_journal", null)
        cursorDebug2.moveToFirst()
        var count : Long = 0
        while (!cursorDebug2.isAfterLast) {
            println(cursorDebug2.getString(cursorDebug2.getColumnIndex("id"))
                    + " " + cursorDebug2.getString(cursorDebug2.getColumnIndex("description")) + " "
                    + cursorDebug2.getLong(cursorDebug2.getColumnIndex("time"))
                    + " " + cursorDebug2.getLong(cursorDebug2.getColumnIndex("name")))
            count++
            cursorDebug2.moveToNext()
        }
        println("HOW MANY IN EVENT JOURNAL " + count)
        cursorDebug2.close()
        println("LAST ENTRY DATE")
        // FIXME СКАЗАТЬ ДАНЕ, ЧТОБЫ ОН ВСЕГДА ИНИЦИАЛИЗИРОВАЛ ВРЕМЯ КОНЦА СОБЫТИЯ, ДАЖЕ ЕСЛИ ОНО НЕ УКАЗАНО
        // FIXME SUCCESSFUL MUST BE NOT NULL*/
    }

    fun getEntriesForGivenPeriodOfTime(start : Long, end : Long, mDb : SQLiteDatabase) : MutableList<JournalEntry> {
        var entries : MutableList<JournalEntry> = LinkedList()
        val cursor = mDb.rawQuery(
                "SELECT * FROM event_journal WHERE (time > $start AND time < $end);",
                null)
        cursor.use { cursor ->
            cursor.moveToFirst()
            var count: Long = 0
            while (!cursor.isAfterLast) {
                entries.add(mapEntry(cursor, mDb))
                count++
                cursor.moveToNext()
            }
            println(count)
            cursor.close()
        }
        /*} catch (e : Exception) {
            println("Table is empty")
        }*/
        return entries
    }

    fun mapEntry(cursor : Cursor, mDb : SQLiteDatabase) : JournalEntry {
        var e : JournalEntry = JournalEntry()
        e.setName(cursor.getString(cursor.getColumnIndex("name")))
        e.setID(cursor.getLong(cursor.getColumnIndex("id")))
        e.setDescription(cursor.getString(cursor.getColumnIndex("description")))
        e.setTime(cursor.getLong(cursor.getColumnIndex("time")))
        e.setDoneSuccessful(cursor.getInt(cursor.getColumnIndex("successful")))
        /*var result1 : String = "${e.getID()}  ${e.getName()} + ${e.getDescription()} + ${e.getTime()}"
        println(result1)*/
        return e
    }
}