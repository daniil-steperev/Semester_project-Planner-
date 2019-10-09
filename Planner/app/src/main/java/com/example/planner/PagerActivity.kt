package com.example.planner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.planner.fragments.ToDoFragment
import com.example.planner.fragments.MenuFragment
import com.example.planner.fragments.StatisticsFragment

class PagerActivity : AppCompatActivity() {
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
    }
}