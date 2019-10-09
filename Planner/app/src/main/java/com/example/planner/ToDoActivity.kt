package com.example.planner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class ToDoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var llMain : LinearLayout
    private lateinit var addButton : Button
    private lateinit var deleteButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo)

        llMain = findViewById(R.id.todo_window)
        addButton = findViewById(R.id.add_button)
        deleteButton = findViewById(R.id.delete_button)

        addButton.setOnClickListener(this)
        deleteButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_button -> {
                val lParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                lParams.gravity = Gravity.LEFT

                val btnNew = Button(this)
                btnNew.setText("123".toString())
                llMain.addView(btnNew, lParams)
                btnNew.setOnClickListener(this)
            }

            R.id.delete_button -> {
                llMain.removeAllViews()
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
