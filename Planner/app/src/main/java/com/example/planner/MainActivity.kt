package com.example.planner

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.example.planner.gestures.DetectSwipeGesturesListener
import java.util.concurrent.TimeUnit
import android.content.SharedPreferences
import androidx.work.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var gestureDetector : GestureDetectorCompat

    private lateinit var btnToTodo : Button
    private lateinit var btnToCat : Button
    private lateinit var btnToNotes : Button
    private lateinit var btnToCalendar : Button
    private lateinit var btnToStatistics : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnToTodo = findViewById(R.id.btnToToDO)
        btnToCat = findViewById(R.id.btnToCat)
        btnToNotes = findViewById(R.id.btnToNotes)
        btnToCalendar = findViewById(R.id.btnToCalendar)
        btnToStatistics = findViewById(R.id.btnToStatistics)

        btnToTodo.setOnClickListener {view -> onClick(view)}
        btnToCat.setOnClickListener {view -> onClick(view)}
        btnToNotes.setOnClickListener {view -> onClick(view)}
        btnToCalendar.setOnClickListener {view -> onClick(view)}
        btnToStatistics.setOnClickListener {view -> onClick(view)}

        val gestureListener = DetectSwipeGesturesListener(this, ToDoActivity(), StatisticsActivity())
        gestureDetector = GestureDetectorCompat(this, gestureListener)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnToToDO -> {
                startActivity(Intent(this, ToDoActivity::class.java))
            }

            R.id.btnToCat -> {
                startActivity(Intent(this, CatActivity::class.java))
            }

            R.id.btnToCalendar -> {
                startActivity(Intent(this, CalendarActivity::class.java))
            }

            R.id.btnToStatistics -> {
                startActivity(Intent(this, StatisticsActivity::class.java))
            }
        }

        addToJournalPassedEvents()
    }

    private fun addToJournalPassedEvents() {
        val dailyWorkRequest = PeriodicWorkRequestBuilder<EventTimer>(8, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork("Adding events to journal", ExistingPeriodicWorkPolicy.KEEP, dailyWorkRequest)
    }

    /*private fun computateDelay() : Long {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        // Set Execution around 05:00:00 AM
        dueDate.set(Calendar.HOUR_OF_DAY, 5)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        return dueDate.timeInMillis - currentDate.timeInMillis
    }*/
}
