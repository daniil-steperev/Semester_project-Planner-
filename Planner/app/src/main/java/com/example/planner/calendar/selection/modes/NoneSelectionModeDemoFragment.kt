package com.example.planner.calendar.selection.modes

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_demo_selection.*
import ru.cleverpumpkin.calendar.CalendarView
import com.example.planner.calendar.BaseFragment
import com.example.planner.R

/**
 * This demo fragment demonstrate usage of the [CalendarView] with the
 * [CalendarView.SelectionMode.NONE] selection mode.
 *
 * Created by Alexander Surinov on 2019-05-13.
 */
class NoneSelectionModeDemoFragment : BaseFragment() {

    override val layoutRes: Int
        get() = R.layout.fragment_demo_selection

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            calendarView.setupCalendar(selectionMode = CalendarView.SelectionMode.NONE)
        }

        selectedDatesView.visibility = View.GONE
    }

}