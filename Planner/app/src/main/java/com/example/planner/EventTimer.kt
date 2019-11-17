package com.example.planner

import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabaseLockedException
import android.provider.SyncStateContract
import android.util.Log
import androidx.work.Operation.State.SUCCESS
import javax.xml.datatype.DatatypeConstants.SECONDS
import androidx.annotation.NonNull
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.planner.db.Event
import com.example.planner.db.Trigger
import com.example.planner.db.TriggerRule
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.hours


class EventTimer(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    private val context : Context = appContext

    override fun doWork(): Result {
        var date : Date = Date(System.currentTimeMillis())
        println("doWork: start")
        var connection = DatabaseWorker()
        //if (connection)
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
        event.setName("Alloha!")
        event.setDescription("Dance!!!")
        event.setTime(1573673655014)

        var chosenTriggers: MutableList<Trigger> = LinkedList<Trigger>()
        chosenTriggers.add(Trigger(4, TriggerRule.THURSDAY))
        connection.addEvent(event, chosenTriggers)
        connection.closeConnection()
        connection.getmDb().close()
        println("doWork: end")

        return Result.success()
    }

}