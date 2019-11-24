package com.example.planner

import android.content.Context
import android.database.sqlite.SQLiteDatabaseLockedException
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.planner.db.Event
import com.example.planner.db.Trigger
import com.example.planner.db.TriggerRule
import java.util.*


class EventTimer(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    private val context : Context = appContext

    override fun doWork(): Result {
        var date : Date = Date(System.currentTimeMillis())
        println("doWork: start")
        var connection = DatabaseWorker()
        //if connection is locked, timer waits, until it will no be free
        var dbLocked : Boolean = true
        while (dbLocked) {
            try {
                connection.setConnection(context)
                dbLocked = false
            } catch (e: SQLiteDatabaseLockedException) {
                Thread.sleep(10)
            }
        }

        var event = Event()
        var time = System.currentTimeMillis()
        event.setName("Alloha! + $time ")
        event.setDescription("Dance!!!")
        event.setTime(System.currentTimeMillis())

        var chosenTriggers: MutableList<TriggerRule> = mutableListOf()
        chosenTriggers.add(TriggerRule.WEDNESDAY)
        connection.addEvent(event, chosenTriggers)

        connection.addEventsToJournal()
        connection.closeConnection()
        connection.getmDb().close()

        println("doWork: end")

        return Result.success()
    }

}