package com.example.planner.db

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.graphics.toColorLong
import kotlinx.coroutines.processNextEventInCurrentThread
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

        while (lastEntryDate < currentTime) {
            var triggers : MutableList<Trigger> = triggerService.readTrigger(mDb, lastEntryDate)
            var list : List<Event> =  eventService.readEvent(triggers, mDb)
            for (i in list) {

                var lastEntryDate : Date = Date(lastEntryDate)
                var originalDate : Date = Date(i.getTime())
                var finalDate : Date = Date()
                finalDate.year = lastEntryDate.year
                finalDate.month = lastEntryDate.month
                finalDate.date = lastEntryDate.date
                finalDate.minutes = originalDate.minutes
                finalDate.hours = originalDate.hours

                var shift = Calendar.getInstance()
                shift.setTime(finalDate)
                val query1 = "INSERT INTO event_journal (name, time, description, successful) " +
                        "VALUES(\"${i.getName()}\", \"${shift.timeInMillis}\", \"${i.getDescription()}\", 1); "
                mDb.beginTransaction()
                try {
                    mDb.execSQL(query1)
                    mDb.setTransactionSuccessful()
                } finally {
                    mDb.endTransaction()
                }
            }
            lastEntryDate += 86400000
        }
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