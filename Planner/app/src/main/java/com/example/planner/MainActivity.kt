package com.example.planner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.work.*
import com.example.planner.fragments.MenuFragment
import com.example.planner.fragments.StatisticsFragment
import com.example.planner.fragments.ToDoFragment
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var pager : ViewPager
    private lateinit var pageAdapter : PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pager)

        var list : MutableList<Fragment> = mutableListOf()
        val pageFragment1 = ToDoFragment()
        val pageFragment2 = MenuFragment()
        val pageFragment3 = StatisticsFragment()
        list.add(pageFragment1)
        list.add(pageFragment2)
        list.add(pageFragment3)

        pager = findViewById(R.id.pager)
        pageAdapter = SlidePagerAdapter(supportFragmentManager, list, this)

        pager.adapter = pageAdapter
        addToJournalPassedEvents()
    }

    private fun addToJournalPassedEvents() {
        //val delay = computateDelay()
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
