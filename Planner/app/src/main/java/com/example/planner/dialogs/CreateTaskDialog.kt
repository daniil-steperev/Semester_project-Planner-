package com.example.planner.dialogs

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.planner.DatabaseWorker
import com.example.planner.R
import com.example.planner.Task
import com.example.planner.ToDoActivity
import com.example.planner.db.Event
import com.example.planner.db.EventService
import com.example.planner.db.TriggerRule
import java.util.*
import kotlin.math.min

class CreateTaskDialog(private val toDoActivity: ToDoActivity) : DialogFragment(), View.OnClickListener {
    var isReady = false;

    private var task = Task()

    private lateinit var addTaskButton : Button

    private lateinit var taskEditor : EditText
    private lateinit var timePicker : TimePicker

    private lateinit var cBoxMonday : CheckBox
    private lateinit var cBoxTuesday : CheckBox
    private lateinit var cBoxWednesday : CheckBox
    private lateinit var cBoxThursday : CheckBox
    private lateinit var cBoxFriday : CheckBox
    private lateinit var cBoxSaturday : CheckBox
    private lateinit var cBoxSunday : CheckBox
    private lateinit var cBoxEveryDay : CheckBox
    private lateinit var cBoxEveryWeek : CheckBox

    private lateinit var activityContext : Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isReady = false

        val view = inflater.inflate(R.layout.create_task, null)

        addTaskButton = view.findViewById(R.id.create_task_button)

        taskEditor = view.findViewById(R.id.task_editor)
        timePicker = view.findViewById(R.id.time_picker)
        timePicker.setIs24HourView(true)

        cBoxMonday = view.findViewById(R.id.monday_repeat)
        cBoxTuesday = view.findViewById(R.id.tuesday_repeat)
        cBoxWednesday = view.findViewById(R.id.wednesday_repeat)
        cBoxThursday = view.findViewById(R.id.thursday_repeat)
        cBoxFriday = view.findViewById(R.id.friday_repeat)
        cBoxSaturday = view.findViewById(R.id.saturday_repeat)
        cBoxSunday = view.findViewById(R.id.sunday_repeat)
        cBoxEveryDay = view.findViewById(R.id.every_day_repeat)
        cBoxEveryWeek = view.findViewById(R.id.every_week_repeat)

        setOnClick()

        return view
    }

    private fun setOnClick() {
        addTaskButton.setOnClickListener(this)
        cBoxMonday.setOnClickListener(this)
        cBoxTuesday.setOnClickListener(this)
        cBoxWednesday.setOnClickListener(this)
        cBoxThursday.setOnClickListener(this)
        cBoxFriday.setOnClickListener(this)
        cBoxSaturday.setOnClickListener(this)
        cBoxSunday.setOnClickListener(this)
        cBoxEveryWeek.setOnClickListener(this)
        cBoxEveryDay.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v) {
            cBoxMonday -> addOrRemoveRule(TriggerRule.MONDAY)
            cBoxTuesday -> addOrRemoveRule(TriggerRule.TUESDAY)
            cBoxWednesday -> addOrRemoveRule(TriggerRule.WEDNESDAY)
            cBoxThursday-> addOrRemoveRule(TriggerRule.THURSDAY)
            cBoxFriday -> addOrRemoveRule(TriggerRule.FRIDAY)
            cBoxSaturday -> addOrRemoveRule(TriggerRule.SATURDAY)
            cBoxSunday -> addOrRemoveRule(TriggerRule.SUNDAY)
            cBoxEveryDay -> addOrRemoveRule(TriggerRule.DAILY)
            cBoxEveryWeek -> addOrRemoveRule(TriggerRule.WEEKLY)

            addTaskButton -> {
                val taskName = taskEditor.text.toString()

                task.setTask(taskName)

                val hour = timePicker.currentHour
                val minute = timePicker.currentMinute

                addToDataBase(hour, minute)
                toDoActivity.updateTaskList()

                dismiss() // close create task dialog fragment
            }
        }
    }

    private fun addOrRemoveRule(rule : TriggerRule) {
        if (task.isRuleContains(rule)) {
            task.removeRule(rule)
        } else {
            task.addRule(rule)
        }
    }

    /** A method that adds Event to database. */
    private fun addToDataBase(hour : Int, minute : Int) {
        val connection = DatabaseWorker()
        connection.setConnection(activityContext)

        val calendar = GregorianCalendar()
        calendar.timeInMillis = System.currentTimeMillis()

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)


        val timeLong = calendar.timeInMillis // FIXME: тут надо получить unix время из времени
        val eventName = task.getTask()
        val eventDescription = "" // FIXME: тут надо получать описание event
        val newEvent = Event(eventName, eventDescription, timeLong)

        val activeTriggers = task.getRule()
        val eventService = EventService()

        eventService.addEvent(newEvent, connection.getmDb(), activeTriggers)
    }
}
