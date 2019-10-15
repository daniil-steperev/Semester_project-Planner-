package com.example.planner.calendar.dateboundaries

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_calendar.*
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import com.example.planner.calendar.BaseFragment
import com.example.planner.R
import java.util.*

/**
 * This demo fragment demonstrate usage of the [CalendarView] with min-max date boundaries.
 *
 * Created by Alexander Surinov on 2019-05-13.
 */
class DateBoundariesDemoFragment : BaseFragment() {

    override val layoutRes: Int
        get() = R.layout.fragment_calendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(toolbarView) {
            setTitle(R.string.demo_date_boundaries)
            setNavigationIcon(R.drawable.ic_arrow_back_24dp)
            setNavigationOnClickListener { activity?.onBackPressed() }
        }

        if (savedInstanceState == null) {
            setupCalendar()
        }
    }

    private fun setupCalendar() {
        val calendar = Calendar.getInstance()

        calendar.set(2018, Calendar.JUNE, 1)
        val initialDate = CalendarDate(calendar.time)

        calendar.set(2018, Calendar.MAY, 28)
        val minDate = CalendarDate(calendar.time)

        calendar.set(2018, Calendar.JULY, 2)
        val maxDate = CalendarDate(calendar.time)

        calendarView.setupCalendar(
            initialDate = initialDate,
            minDate = minDate,
            maxDate = maxDate,
            selectionMode = CalendarView.SelectionMode.MULTIPLE
        )

    }

}