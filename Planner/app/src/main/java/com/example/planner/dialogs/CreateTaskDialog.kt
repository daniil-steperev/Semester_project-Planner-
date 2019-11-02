package com.example.planner.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.planner.R
import com.example.planner.Task
import com.example.planner.db.Trigger
import com.example.planner.db.TriggerRule
import java.sql.Date
import java.sql.Time

class CreateTaskDialog : DialogFragment(), View.OnClickListener {
    var isReady = false;

    private lateinit var task : Task

    private lateinit var addTaskButton : Button

    private lateinit var taskEditor : EditText
    private lateinit var timeEditor : EditText

    private lateinit var cBoxMonday : CheckBox
    private lateinit var cBoxTuesday : CheckBox
    private lateinit var cBoxWednesday : CheckBox
    private lateinit var cBoxThursday : CheckBox
    private lateinit var cBoxFriday : CheckBox
    private lateinit var cBoxSaturday : CheckBox
    private lateinit var cBoxSunday : CheckBox
    private lateinit var cBoxEveryDay : CheckBox
    private lateinit var cBoxEveryWeek : CheckBox

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isReady = false

        val view = inflater.inflate(R.layout.create_task, null)
        view.findViewById<Button>(R.id.create_task_button).setOnClickListener(this)

        addTaskButton = view.findViewById(R.id.create_task_button)

        taskEditor = view.findViewById(R.id.task_editor)
        timeEditor = view.findViewById(R.id.time_editor)

        cBoxMonday = view.findViewById(R.id.monday_repeat)
        cBoxTuesday = view.findViewById(R.id.tuesday_repeat)
        cBoxWednesday = view.findViewById(R.id.wednesday_repeat)
        cBoxThursday = view.findViewById(R.id.thursday_repeat)
        cBoxFriday = view.findViewById(R.id.friday_repeat)
        cBoxSaturday = view.findViewById(R.id.saturday_repeat)
        cBoxSunday = view.findViewById(R.id.sunday_repeat)
        cBoxEveryDay = view.findViewById(R.id.every_day_repeat)
        cBoxEveryWeek = view.findViewById(R.id.every_week_repeat)

        return view
    }

    override fun onClick(v: View?) {
        task = Task()

        when (view) { // FIXME: double click on rule?
            cBoxMonday -> task.setRule(TriggerRule.MONDAY)
            cBoxTuesday -> task.setRule(TriggerRule.TUESDAY)
            cBoxWednesday -> task.setRule(TriggerRule.TUESDAY)
            cBoxThursday-> task.setRule(TriggerRule.THURSDAY)
            cBoxFriday -> task.setRule(TriggerRule.FRIDAY)
            cBoxSaturday -> task.setRule(TriggerRule.SATURDAY)
            cBoxSunday -> task.setRule(TriggerRule.SUNDAY)
            cBoxEveryDay -> task.setRule(TriggerRule.DAILY)
            cBoxEveryWeek -> task.setRule(TriggerRule.WEEKLY)

            addTaskButton -> {
                val time = timeEditor.text.toString()
                val taskName = taskEditor.text.toString()

                task.setTask(taskName)
                task.setTime(time)
            }
        }
    }

    fun getTask() : Task {
        isReady = true
        return task
    }
}