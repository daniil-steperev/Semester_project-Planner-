package com.example.planner

import android.os.Bundle
import com.example.planner.gestures.BaseSwipeToDismissActivity

class StatisticsActivity : BaseSwipeToDismissActivity() {
    override fun getLayoutId(): Int {
        return R.layout.statistics
    }

    override fun isActivityDraggable(): Boolean {
        return true
    }

    private var x1 : Float = 0.toFloat()
    private var x2 : Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
    }
}
