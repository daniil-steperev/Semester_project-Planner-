package com.example.planner.calendar

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.planner.R
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.DefaultEventAdapter
import com.ognev.kotlin.agendacalendarview.utils.DateHelper
import java.text.SimpleDateFormat
import java.util.*

/**
 * Sample event adapter
 */
class SampleEventAgendaAdapter(private var context: Context) : DefaultEventAdapter() {
    private var format: SimpleDateFormat = SimpleDateFormat(context.getString(R.string.header_date), Locale.ENGLISH)

    override fun getHeaderLayout() = R.layout.view_agenda_header

    override fun getHeaderItemView(view: View, day: Calendar) {
        val txtDayOfMonth = view.findViewById(R.id.view_agenda_day_of_month) as TextView
        val today = CalendarManager.instance!!.today

        if (DateHelper.sameDate(day, today)) {
            txtDayOfMonth.setTextColor(ContextCompat.getColor(context,
                R.color.main_blue
            ))
        } else {
            txtDayOfMonth.setTextColor(ContextCompat.getColor(context,
                R.color.text_light_color
            ))
        }

        txtDayOfMonth.text = format.format(day.time)
    }

    override fun getEventItemView(view: View, event: CalendarEvent, position: Int) {
        val myEvent = event as MyCalendarEvent
        val myObject: SampleEvent = myEvent.event as SampleEvent
        val date : Date = Date(myObject.time)

        if (myEvent.hasEvent()) {
            val name = view.findViewById(R.id.name) as TextView
            val description = view.findViewById(R.id.description) as TextView
            val time = view.findViewById(R.id.time) as TextView

            name.text = myObject?.name
            description.text = myObject?.description

            var hours = ""
            if (date.hours < 10) {
                 hours = "0" + date.hours.toString()
            } else {
                hours = date.hours.toString()
            }

            var minutes = ""
            if (date.minutes < 10) {
                minutes = "0" + date.minutes.toString()
            } else {
                minutes = date.minutes.toString()
            }

            time.text = hours + ":" + minutes
        }

        view.setOnClickListener {
            Toast.makeText(view.context, "Item: ".plus(position), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getEventLayout(hasEvent: Boolean): Int {
        return if (hasEvent) R.layout.view_agenda_event else R.layout.view_agenda_empty_event
    }
}