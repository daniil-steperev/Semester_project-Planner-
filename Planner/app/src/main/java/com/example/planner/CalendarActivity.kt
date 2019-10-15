package com.example.planner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.planner.calendar.customstyle.CodeStylingDemoFragment
import com.example.planner.calendar.dateboundaries.DateBoundariesDemoFragment
import com.example.planner.calendar.demolist.DemoItem
import com.example.planner.calendar.demolist.DemoListFragment
import com.example.planner.calendar.dialogue.DialogDemoFragment
import com.example.planner.calendar.events.EventListDemoFragment
import com.example.planner.calendar.selection.SelectionModesDemoFragment

class CalendarActivity : AppCompatActivity(), DemoListFragment.OnDemoItemSelectionListener  {

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val calendar = Calendar.getInstance()
        val initialDate = CalendarDate(calendar.time)
        //println(calendar.time.toString() + "LALALA")

        calendar_view.setupCalendar(selectionMode = CalendarView.SelectionMode.SINGLE, initialDate = initialDate)


        calendar_view.onDateClickListener = { date ->
            Log.d(Log.INFO.toString(), date.toString())
            val indicatorsForDate = calendar_view.getDateIndicators(date)
            for (i in indicatorsForDate) {
                println(i.color)
            }
            // Do something ...
        }
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, DemoListFragment())
                .commit()
        }
    }

    override fun onDemoItemSelected(demoItem: DemoItem) {
        val demoFragment = when (demoItem) {
            DemoItem.SELECTION -> SelectionModesDemoFragment()
            DemoItem.DATE_BOUNDARIES -> DateBoundariesDemoFragment()
            DemoItem.STYLING -> CodeStylingDemoFragment()
            DemoItem.EVENTS -> EventListDemoFragment()
            DemoItem.DIALOG -> DialogDemoFragment()
        }

        if (demoFragment is DialogDemoFragment) {
            demoFragment.show(supportFragmentManager, null)
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, demoFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }
    }
}
