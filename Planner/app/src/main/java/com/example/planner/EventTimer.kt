package com.example.planner

import android.content.ContentValues.TAG
import android.content.Context
import android.provider.SyncStateContract
import android.util.Log
import androidx.work.Operation.State.SUCCESS
import javax.xml.datatype.DatatypeConstants.SECONDS
import androidx.annotation.NonNull
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit


class EventTimer(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        Log.d(TAG, "doWork: start")
        println("doWork: start")
        try {
            TimeUnit.SECONDS.sleep(10)
        } catch (e: Exception) {
            return Result.failure()
        }
        Log.d(TAG, "doWork: end")
        println("doWork: end")

        return Result.success()
    }

}