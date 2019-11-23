package com.example.planner.gestures

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.planner.R
import com.r0adkll.slidr.Slidr


abstract class BaseSwipeToDismissActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        if (isActivityDraggable()) {
            Slidr.attach(this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isActivityDraggable()) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        }
    }

    abstract fun getLayoutId() : Int;
    abstract fun isActivityDraggable() : Boolean;
}