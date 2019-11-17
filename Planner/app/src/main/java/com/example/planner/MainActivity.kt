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
        /*val mWorkManager = WorkManager.getInstance()
        val myWorkRequest = PeriodicWorkRequestBuilder<EventTimer>(
            15, TimeUnit.MINUTES, 20, TimeUnit.MINUTES
        ).build()
        mWorkManager.enqueueUniquePeriodicWork("Test adding", ExistingPeriodicWorkPolicy.KEEP, myWorkRequest)*/
        val mWorkManager = WorkManager.getInstance()
        val myWorkRequest = OneTimeWorkRequestBuilder<EventTimer>()
            .setInitialDelay(2, TimeUnit.MINUTES)
            .build()
        mWorkManager.enqueueUniqueWork("Test adding", ExistingWorkPolicy.KEEP, myWorkRequest)
    }
}
