package com.example.planner.todo

import android.graphics.Color
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


class CurrentDate(
    private var day: TextView,
    private var month: TextView,
    private var dayOfWeek: TextView,
    private var time: TextView
) {
    lateinit var calendar : GregorianCalendar

    fun updateDate() {
        calendar = GregorianCalendar()
        calendar.timeInMillis = System.currentTimeMillis()

        day.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        day.setTextColor(Color.BLACK)
        month.text = getMonth(calendar.get(Calendar.MONTH))
        month.setTextColor(Color.BLACK)
        dayOfWeek.text = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        dayOfWeek.setTextColor(Color.BLACK)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        time.text = timeFormat.format(calendar.time)
        time.setTextColor(Color.BLACK)
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