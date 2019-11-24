package com.example.planner.dialogs

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import com.example.planner.DatabaseWorker
import com.example.planner.R
import com.example.planner.Task
import com.example.planner.ToDoActivity
import com.example.planner.db.Event
import com.example.planner.db.EventService

class RemoveTaskDialog(private val tasks : ArrayList<Task>, private val activity : ToDoActivity) : DialogFragment(), View.OnClickListener {
    private lateinit var removingTasks : MutableList<Task>
    private lateinit var checkBoxList : MutableList<CheckBox>
    private lateinit var taskList: MutableList<Task>

    private lateinit var window : LinearLayout
    private lateinit var removeButton : Button

    override fun onClick(v: View?) {
        removingTasks = mutableListOf()

        for (i in checkBoxList.indices) {
            if (checkBoxList[i].isChecked) {
                removingTasks.add(taskList[i])
            }
        }

        removeFromDataBase()

        activity.updateTaskList()
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.remove_task_dialog, null)

        checkBoxList = mutableListOf()
        window = view.findViewById(R.id.remove_field)
        removeButton = view.findViewById(R.id.remove_button)
        removeButton.setOnClickListener(this)

        taskList = tasks
        addCheckBoxesToScreen()

        return view
    }

    private fun addCheckBoxesToScreen() {
        val lParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        for (task in taskList) {
            val newLine = getNewCheckBoxLine(task)
            window.addView(newLine, lParams)
        }
    }

    private fun getNewCheckBoxLine(task : Task) : LinearLayout {
        val timeText = task.getTime()
        val taskText = task.getTask()
        val checkBox = CheckBox(activity)
        checkBoxList.add(checkBox)

        val newLine = LinearLayout(activity)
        newLine.orientation = LinearLayout.HORIZONTAL

        val time = TextView(activity)
        time.setPadding(5)
        time.text = timeText.toString()
        time.textSize = 20.toFloat()

        val task = TextView(activity)
        task.setPadding(5)
        task.gravity = Gravity.CENTER
        task.text = taskText
        task.textSize = 20.toFloat()

        val timeLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

        val taskLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 3f)

        newLine.addView(checkBox, timeLayoutParams)
        newLine.addView(time, timeLayoutParams)
        newLine.addView(task, taskLayoutParams)

        return newLine
    }

    private fun removeFromDataBase() {
        val connection = DatabaseWorker()
        connection.setConnection(activity)

        val eventService = EventService()
        val dataBase = connection.getmDb()

        for (task in removingTasks) {
            val eventList = getAllEvents(dataBase, task.getTask())
            for (event in eventList) {
                eventService.deleteEvent(event, dataBase)
            }
        }
    }

    private fun getAllEvents(dataBase : SQLiteDatabase, eventName : String) : MutableList<Event> {
        var eventList = mutableListOf<Event>()

        println("TASK NAME = $eventName")
        val eventQuery = "SELECT * FROM event WHERE name = \'$eventName\'"
        val eventCursor = dataBase.rawQuery(eventQuery, null)
        eventCursor.moveToFirst()

        while (!eventCursor.isAfterLast) {
            val event = Event()
            event.setID(eventCursor.getLong(eventCursor.getColumnIndex("id")))
            event.setName(eventCursor.getString(eventCursor.getColumnIndex("name")))

            println("EVENT ID = ${event.getID()}")
            println("EVENT NAME = ${event.getName()}")

            eventList.add(event)
            eventCursor.moveToNext()
        }
        eventCursor.close()

        return eventList
    }
}