package com.example.planner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_calendar.*
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import java.util.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val calendar = Calendar.getInstance()
        val initialDate = CalendarDate(calendar.time)
        //println(calendar.time.toString() + "LALALA")

        calendar_view.setupCalendar(selectionMode = CalendarView.SelectionMode.SINGLE, initialDate = initialDate)
        calendar_view.onDateClickListener = { date ->
            Log.d(Log.INFO.toString(), date.toString())
            // Do something ...
            // for example get list of selected dates
            //println(date.toString())
        }
    }
}
