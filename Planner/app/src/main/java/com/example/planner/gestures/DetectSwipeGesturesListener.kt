package com.example.planner.gestures

import android.app.Activity
import android.content.Intent
import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class DetectSwipeGesturesListener(
    private val mainActivity: Activity,
    private val leftActivity: Activity,
    private val rightActivity: Activity
) : GestureDetector.SimpleOnGestureListener() {
    private val MIN_SWIPE_DISTANCE = 100
    private val MAX_SWIPE_DISTANCE = 10000

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        var deltaX : Float = (e1?.x ?: 0.toFloat()) - (e2?.x ?: 0.toFloat())
        var absDeltaX = abs(deltaX)

        if (absDeltaX >= MIN_SWIPE_DISTANCE && absDeltaX <= MAX_SWIPE_DISTANCE) {
            if (deltaX > 0) {
                mainActivity.startActivity(Intent(mainActivity, rightActivity::class.java))
                println("SWIPE LEFT HERE")
            } else {
                mainActivity.startActivity(Intent(mainActivity, leftActivity::class.java))
            }
        }

        return true
    }
}