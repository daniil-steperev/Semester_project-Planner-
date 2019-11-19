package com.example.planner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent

class StatisticsActivity : AppCompatActivity() {
    private var x1 : Float = 0.toFloat()
    private var x2 : Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
    }

    override fun onTouchEvent(touchEvent : MotionEvent) : Boolean {
        when (touchEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = touchEvent.x
            }

            MotionEvent.ACTION_UP -> {
                x2 = touchEvent.x

                if (x1 < x2) {
                    startActivity(Intent(this, CalendarActivity::class.java))
                } else {
                    startActivity(Intent(this, PagerActivity::class.java))
                }
            }
        }

        return false
    }
}
