package com.example.planner

import android.widget.TextView
import com.example.planner.ToDoActivity.TimeThread
import java.text.SimpleDateFormat
import java.util.*


class CurrentDate(
    private var day: TextView,
    private var month: TextView,
    private var dayOfWeek: TextView,
    private var time: TextView
) {
    fun updateDate() {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = System.currentTimeMillis()
        //calendar.set(Calendar.HOUR_OF_DAY, 24)

        day.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        month.text = getMonth(calendar.get(Calendar.MONTH))
        dayOfWeek.text = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))

        val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        time.text = timeFormat.format(calendar.time)
    }

    private fun getDayOfWeek(dayInt : Int) : String {
        when (dayInt) {
            2 -> return "Monday"
            3 -> return "Tuesday"
            4 -> return "Wednesday"
            5 -> return "Thursday"
            6 -> return "Friday"
            7 -> return "Saturday"
            1 -> return "Sunday"
        }

        return "Monday"
    }

    private fun getMonth(monthInt : Int) : String {
        when (monthInt) {
            0 -> return "January"
            1 -> return "February"
            2 -> return "March"
            3 -> return "April"
            4 -> return "May"
            5 -> return "June"
            6 -> return "July"
            7 -> return "August"
            8 -> return "September"
            9 -> return "October"
            10 -> return "November"
            11 -> return "December"
        }

        return "January"
    }
}