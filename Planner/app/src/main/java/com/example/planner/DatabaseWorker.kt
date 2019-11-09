package com.example.planner

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.planner.db.Event
import com.example.planner.db.*
import java.io.IOException


/** Implements specific methods for interaction with app. Wrapper over EventService functionality and TriggerService functionality. */
class DatabaseWorker {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var mDb: SQLiteDatabase
    private var eventService : EventService = EventService()
    private var triggerService : TriggerService = TriggerService()

    fun setConnection(context : Context) {
        mDBHelper = DatabaseHelper(context)

        try {
            mDBHelper.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        }


        try {
            mDb = mDBHelper.writableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }
    }

    fun getmDb() : SQLiteDatabase {
        return mDb
    }

    fun addEvent(e : Event, trigger : MutableList<Trigger>) {
        println("In DBConnection")
        eventService.addEvent(e, mDb, mDBHelper, trigger)
    }

    fun deleteEvent(e : Event) {
        println("In DBConnection")
        eventService.deleteEvent(e, mDb, mDBHelper)
    }

    fun readEventsForToday() : List<Event> {
        return eventService.readEvent(mDb)
    }

    fun closeConnection() {
        mDBHelper.close()
    }

    fun readTriggerForToday() : MutableList<Trigger> {
        return triggerService.readTrigger(mDb, System.currentTimeMillis())
    }
}