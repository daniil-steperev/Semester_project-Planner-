package com.example.planner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.planner.gestures.BaseSwipeToDismissActivity

class ShopActivity : BaseSwipeToDismissActivity() {

    override fun getLayoutId(): Int {
        return R.layout.statistics
    }

    override fun isActivityDraggable(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
    }
}
