package com.example.planner.todo.dialogs

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabaseLockedException
import android.graphics.Color
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
import com.example.planner.todo.Task
import com.example.planner.todo.ToDoActivity
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

        lParams.setMargins(10, 20, 10, 20)

        for (task in taskList) {
            val newLine = getNewCheckBoxLine(task)
            newLine.setBackgroundResource(R.drawable.menu_button)
            window.addView(newLine, lParams)
        }
    }

    private fun getNewCheckBoxLine(task : Task) : LinearLayout {
        val timeText = task.getTime().toString()
        val taskText = task.getTask()
        val checkBox = CheckBox(activity)
        checkBoxList.add(checkBox)

        var newLine = LinearLayout(activity)
        newLine.orientation = LinearLayout.HORIZONTAL

        val time = TextView(activity)
        time.setPadding(5)
        val timeWithoutSeconds = timeText.subSequence(0, timeText.lastIndexOf(':'))
        time.text = timeWithoutSeconds
        time.textSize = 20.toFloat()
        time.setTextColor(Color.BLACK)

        val task = TextView(activity)
        task.setPadding(5)
        task.gravity = Gravity.CENTER
        task.text = taskText
        task.textSize = 20.toFloat()
        task.setTextColor(Color.BLACK)

        val timeLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

        val taskLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 3f)

        newLine.addView(checkBox, timeLayoutParams)
        newLine.addView(time, timeLayoutParams)
        newLine.addView(task, taskLayoutParams)

        newLine.setPadding(5, 10, 5, 10)

        return newLine
    }

    private fun removeFromDataBase() {
        var dbLocked = true
        val connection = DatabaseWorker()
        while (dbLocked) {
            try {
                connection.setConnection(activity)
                dbLocked = false
            } catch (e: SQLiteDatabaseLockedException) {
                Thread.sleep(10)
            }
        }

        val eventService = EventService()
        val dataBase = connection.getmDb()

        for (task in removingTasks) {
            val eventList = getAllEvents(dataBase, task.getTask())
            for (event in eventList) {
                eventService.deleteEvent(event, dataBase)
            }
        }

        connection.closeConnection()
        connection.getmDb().close()
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