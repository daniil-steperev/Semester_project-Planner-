package com.example.planner.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.planner.R
import com.example.planner.ToDoActivity


class ToDoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val intent = Intent(activity, ToDoActivity::class.java)
        //startActivity(intent)
        return inflater.inflate(R.layout.todo, container, false) as ViewGroup
    }
}