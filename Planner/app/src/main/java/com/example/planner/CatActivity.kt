package com.example.planner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cat.*
import android.util.Log
import android.util.Log.INFO
import com.example.planner.gestures.BaseSwipeToDismissActivity

class CatActivity : BaseSwipeToDismissActivity() {

    override fun getLayoutId(): Int {
        return R.layout.statistics
    }

    override fun isActivityDraggable(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat)
        shop.setOnClickListener {
            Log.d(INFO.toString(), "button shop pressed")
            val myIntent = Intent(this, ShopActivity::class.java)
            startActivity(myIntent)
        }
    }
}
