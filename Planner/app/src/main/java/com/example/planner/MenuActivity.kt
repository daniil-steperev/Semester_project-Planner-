package com.example.planner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.menu.view.*

class MenuActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnToTodo : Button
    private lateinit var btnToCat : Button
    private lateinit var btnToNotes : Button
    private lateinit var btnToCalendar : Button
    private lateinit var btnToStatistics : Button

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_menu)

        btnToTodo = findViewById(R.id.btnToToDO)
        btnToCat = findViewById(R.id.btnToCat)
        btnToNotes = findViewById(R.id.btnToNotes)
        btnToCalendar = findViewById(R.id.btnToCalendar)
        btnToStatistics = findViewById(R.id.btnToStatistics)

        btnToTodo.setOnClickListener {view -> onClick(view)}
        btnToCat.setOnClickListener {view -> onClick(view)}
        btnToNotes.setOnClickListener {view -> onClick(view)}
        btnToCalendar.setOnClickListener {view -> onClick(view)}
        btnToStatistics.setOnClickListener {view -> onClick(view)}
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnToToDO -> {
                startActivity(Intent(this, ToDoActivity::class.java))
            }

            R.id.btnToCat -> {
                startActivity(Intent(this, CatActivity::class.java))
            }

            R.id.btnToCalendar -> {
                startActivity(Intent(this, CalendarActivity::class.java))
            }

            R.id.btnToStatistics -> {
                startActivity(Intent(this, StatisticsActivity::class.java))
            }
        }

    }
}
