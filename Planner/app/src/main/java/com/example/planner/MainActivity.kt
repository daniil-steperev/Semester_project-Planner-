package com.example.planner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import com.example.planner.fragments.MenuFragment
import com.example.planner.fragments.StatisticsFragment
import com.example.planner.fragments.ToDoFragment
import com.example.planner.gestures.DetectSwipeGesturesListener

class MainActivity : AppCompatActivity() {
    private lateinit var gestureDetector : GestureDetectorCompat

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

        val gestureListener = DetectSwipeGesturesListener(this, ToDoActivity(), StatisticsActivity())
        gestureDetector = GestureDetectorCompat(this, gestureListener)

            //pager = findViewById(R.id.pager)
        //pageAdapter = SlidePagerAdapter(supportFragmentManager, list, this)

        //pager.adapter = pageAdapter
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }
}
