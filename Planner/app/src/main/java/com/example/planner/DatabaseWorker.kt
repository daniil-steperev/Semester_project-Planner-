package com.example.planner

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.planner.db.Event
import com.example.planner.db.*
import android.util.Log
import java.io.*


/** Implements specific methods for interaction with app. Wrapper over EventService functionality and TriggerService functionality. */
class DatabaseWorker {
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var mDb: SQLiteDatabase
    private var eventService : EventService = EventService()
    private var triggerService : TriggerService = TriggerService()
    val filename = "last_entry_date"
    val lastDate : Long = 0
    private var journalEntryService : JournalEntryService = JournalEntryService()
    private lateinit var context : Context
    private val FILE_NAME = "config.txt"

    /*constructor(initTime : Long) {
        journalEntryService = JournalEntryService(initTime)
    }*/

    fun setConnection(newContext : Context) {
        context = newContext
        mDBHelper = DatabaseHelper(newContext)

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

    private fun writeToFile(data: String, context: Context) {
        var fos : FileOutputStream
        try {
            fos = context.openFileOutput("config.txt", Context.MODE_PRIVATE)
            fos.write(data.toByteArray())
            println("Saved to " + context.filesDir + "/" + FILE_NAME)
            fos.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        } finally {
            //fos?.close()
        }

    }

    private fun readFromFile(context: Context): String {
        try {
            var fis: FileInputStream = context.openFileInput(FILE_NAME)
            var isr = InputStreamReader(fis)
            var br = BufferedReader(isr)
            var sb: StringBuilder = StringBuilder()
            //var text : String
            sb.append(br.readLine())
            return sb.toString()
        } catch (e : FileNotFoundException) {
            return ""
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

    fun readEventsForToday(date : Long) : List<Event> {
        val triggers = triggerService.readTrigger(mDb, date)
        return eventService.readEvent(triggers, mDb)
    }

    fun closeConnection() {
        mDBHelper.close()
    }

    fun readTriggerForToday() : MutableList<Trigger> {
        return triggerService.readTrigger(mDb, System.currentTimeMillis())
    }



    fun addEventsToJournal() {
        //writeToFile(lastDate.toString(), context)
        println("WE ARE IN ADDING" + readFromFile(context))
        var result = readFromFile(context)
        if (result == "") {
            result = "0"
        }
        journalEntryService.setLastEntryDate(result.toLong())
        println("Set correct last date")
        journalEntryService.addPassedEvents(mDb, eventService, triggerService)
        println("now date is " + journalEntryService.getLastEntryDate())
        writeToFile(journalEntryService.getLastEntryDate().toString(), context)
        println("Read from file " + readFromFile(context))

    }

    fun getEntriesForGivenPeriodOfTime(start : Long, end : Long) : List<JournalEntry> {
        return journalEntryService.getEntriesForGivenPeriodOfTime(start, end, mDb)
    }
}