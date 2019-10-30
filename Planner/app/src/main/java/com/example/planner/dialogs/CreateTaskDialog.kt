package com.example.planner.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.planner.R
import com.example.planner.Task
import java.sql.Date
import java.sql.Time

class CreateTaskDialog : DialogFragment(), View.OnClickListener {
    var isReady = false;

    private lateinit var time : String
    private lateinit var task : String

    private lateinit var taskEditor : EditText
    private lateinit var timeEditor : EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isReady = false

        val view = inflater.inflate(R.layout.create_task, null)
        view.findViewById<Button>(R.id.create_task_button).setOnClickListener(this)

        taskEditor = view.findViewById(R.id.task_editor)
        timeEditor = view.findViewById(R.id.time_editor)

        return view
    }

    override fun onClick(v: View?) {
        time = timeEditor.text.toString()
        task = taskEditor.text.toString()
    }

    fun getTask() : Task {
        isReady = true
        return Task(Date.valueOf("11:11:19"), Time.valueOf("11:11:11"), "abc") // FIXME : DATE
    }
}