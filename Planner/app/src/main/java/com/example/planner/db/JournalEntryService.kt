package com.example.planner.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class JournalEntryService() {
    fun addPassedEvents() {
        //it is called by eventTimer
        //to figure out passed events
        //to make list from them
        //to add this list to table event_journal
    }

    fun returnEventsForGivenPeriodOfTime(start : Long, end : Long) {
        //returns list with events, which happened from start to end
    }

    fun mapEvent(cursor : Cursor, mDb : SQLiteDatabase) : JournalEntry {
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