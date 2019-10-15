package com.example.planner.calendar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import ru.cleverpumpkin.calendar.extension.getColorInt
import com.example.planner.R
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_calendar.fragment_calendar.*

import java.util.*

/**
 * This demo fragment demonstrate usage of the [CalendarView] to display custom events as
 * colored indicators.
 *
 * Created by Alexander Surinov on 2019-05-13.
 */
class EventListDemoFragment : BaseFragment() {

    override val layoutRes: Int
        get() = R.layout.fragment_calendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(toolbarView) {
            setTitle(R.string.demo_events)
            setNavigationIcon(R.drawable.ic_arrow_back_24dp)
            setNavigationOnClickListener { activity?.onBackPressed() }
        }

        calendar_view.datesIndicators = generateEventItems()

        calendar_view.onDateClickListener = { date ->
            showDialogWithEventsForSpecificDate(date)
        }

        if (savedInstanceState == null) {
            calendar_view.setupCalendar(selectionMode = CalendarView.SelectionMode.NONE)
        }
    }

    private fun showDialogWithEventsForSpecificDate(date: CalendarDate) {
        val eventItems = calendar_view.getDateIndicators(date)
            .filterIsInstance<EventItem>()
            .toTypedArray()

        if (eventItems.isNotEmpty()) {
            val adapter = EventDialogAdapter(requireContext(), eventItems)

            val builder = AlertDialog.Builder(requireContext())
                .setTitle("$date")
                .setAdapter(adapter, null)

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun generateEventItems(): List<EventItem> {
        val context = requireContext()
        val calendar = Calendar.getInstance()

        val eventItems = mutableListOf<EventItem>()

        repeat(10) {
            eventItems += EventItem(
                eventName = "Event #1",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.event_1_color)
            )

            eventItems += EventItem(
                eventName = "Event #2",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.event_2_color)
            )

            eventItems += EventItem(
                eventName = "Event #3",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.event_2_color)
            )

            eventItems += EventItem(
                eventName = "Event #4",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.event_2_color)
            )

            eventItems += EventItem(
                eventName = "Event #5",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.event_2_color)
            )

            calendar.add(Calendar.DAY_OF_MONTH, 5)
        }

        return eventItems
    }

}