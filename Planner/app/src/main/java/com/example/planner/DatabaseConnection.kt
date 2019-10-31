package com.example.planner

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.planner.db.Event
import com.example.planner.db.*
import java.io.IOException

class DatabaseConnection {
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

    fun addEvent(e : Event) {
        println("In DBConnection")
        eventService.addEvent(e, mDb)
    }

    /*fun readEventsForToday() {
        //eventService.
    }*/

    fun closeConnection() {
        mDBHelper.close()
    }
}