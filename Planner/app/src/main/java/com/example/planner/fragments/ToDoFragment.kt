package com.example.planner.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.example.planner.R
import com.example.planner.ToDoActivity
import kotlinx.android.synthetic.main.todo.view.*


class ToDoFragment : Fragment(), View.OnClickListener {
    private lateinit var llMain : LinearLayout
    private lateinit var addButton : Button
    private lateinit var deleteButton : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val intent = Intent(activity, ToDoActivity::class.java)
        //startActivity(intent)
        val v = inflater.inflate(R.layout.todo, container, false) as ViewGroup
        llMain = v.todo_window
        addButton = v.add_button
        deleteButton = v.delete_button

        addButton.setOnClickListener {view -> onClick(view)}
        deleteButton.setOnClickListener {view -> onClick(view)}

        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_button -> {
                val lParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                lParams.gravity = Gravity.LEFT

                val btnNew = Button(activity)
                btnNew.setText("123")
                llMain.addView(btnNew, lParams)
                btnNew.setOnClickListener(this)
            }

            R.id.delete_button -> {
                if (llMain.size > 0) {
                    llMain.removeView(llMain[0])
                }
                Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}