package com.example.planner.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.planner.*

import kotlinx.android.synthetic.main.menu.view.*

class MenuFragment : Fragment(), View.OnClickListener {
    private lateinit var btnToTodo : Button
    private lateinit var btnToCat : Button
    private lateinit var btnToNotes : Button
    private lateinit var btnToCalendar : Button
    private lateinit var btnToStatistics : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.menu, container, false) as ViewGroup

        btnToTodo = view.btnToToDO
        btnToCat = view.btnToCat
        btnToNotes = view.btnToNotes
        btnToCalendar = view.btnToCalendar
        btnToStatistics = view.btnToStatistics

        btnToTodo.setOnClickListener {view -> onClick(view)}
        btnToCat.setOnClickListener {view -> onClick(view)}
        btnToNotes.setOnClickListener {view -> onClick(view)}
        btnToCalendar.setOnClickListener {view -> onClick(view)}
        btnToStatistics.setOnClickListener {view -> onClick(view)}

        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnToToDO -> {
                startActivity(Intent(activity, ToDoActivity::class.java))
            }

            R.id.btnToCat -> {
                startActivity(Intent(activity, CatActivity::class.java))
            }

            R.id.btnToCalendar -> {
                startActivity(Intent(activity, CalendarActivity::class.java))
            }

            R.id.btnToStatistics -> {
                startActivity(Intent(activity, StatisticsActivity::class.java))
            }
        }

    }
}
