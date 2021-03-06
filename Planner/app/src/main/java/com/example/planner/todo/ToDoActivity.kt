package com.example.planner.todo

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabaseLockedException
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.setPadding
import com.example.planner.DatabaseWorker
import com.example.planner.R
import com.example.planner.db.EventService
import com.example.planner.todo.dialogs.CreateTaskDialog
import com.example.planner.todo.dialogs.RemoveTaskDialog
import com.example.planner.gestures.BaseSwipeToDismissActivity

@Suppress("DEPRECATION")
class ToDoActivity : BaseSwipeToDismissActivity(), View.OnClickListener {
    private lateinit var llMain : LinearLayout
  
    private lateinit var addButton : Button
    private lateinit var deleteButton : Button

    private lateinit var addedTasks : ArrayList<Task>

    private lateinit var currentDate: CurrentDate
    private lateinit var timeThread : TimeThread

    override fun getLayoutId(): Int {
        return R.layout.statistics
    }

    override fun isActivityDraggable(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo)

        llMain = findViewById(R.id.todo_window)
        addButton = findViewById(R.id.add_button)
        deleteButton = findViewById(R.id.delete_button)
        addedTasks = ArrayList()

        initializeDate()
        updateTaskList()

        addButton.setOnClickListener(this)
        deleteButton.setOnClickListener(this)
    }

    private fun initializeDate() {
        val day = findViewById<TextView>(R.id.day)
        val month = findViewById<TextView>(R.id.month)
        val dayOfWeek = findViewById<TextView>(R.id.day_of_week)
        val time = findViewById<TextView>(R.id.time)

        currentDate = CurrentDate(day, month, dayOfWeek, time)
        currentDate.updateDate()
        timeThread = TimeThread()
        timeThread.start()
    }

    @SuppressLint("ResourceType")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_button -> {
                val createTaskDialog = CreateTaskDialog(this)
                createTaskDialog.show(supportFragmentManager, "createTaskDialog")
            }

            R.id.delete_button -> {
                val removeTaskDialog = RemoveTaskDialog(addedTasks, this)
                removeTaskDialog.show(supportFragmentManager, "removeTaskDialog")
            }
        }
    }

    private fun addToTaskList(newTask : Task) {
        val lParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        lParams.setMargins(10, 20, 10, 20)

        addedTasks.add(newTask)
        addedTasks.sort() // sort tasks

        llMain.removeAllViews()

        for (task in addedTasks) { // add task in right order
            val newLine = getNewTaskLine(task)
            newLine.setBackgroundResource(R.drawable.menu_button)
            llMain.addView(newLine, lParams)
        }
    }

    private fun getNewTaskLine(task : Task) : LinearLayout {
        val timeValue = task.getTime().toString()
        val taskText = task.getTask()

        val newLine = LinearLayout(this)
        newLine.orientation = LinearLayout.HORIZONTAL

        val time = TextView(this)
        time.setPadding(5)

        val timeWithoutSeconds = timeValue.subSequence(0, timeValue.lastIndexOf(':'))
        time.text = timeWithoutSeconds
        time.textSize = 20.toFloat()
        time.setPadding(10, 10, 10, 10)
        time.setTextColor(Color.BLACK)

        val task = TextView(this)
        task.setPadding(5)
        task.gravity = Gravity.CENTER
        task.text = taskText
        task.textSize = 20.toFloat()
        task.setTextColor(Color.BLACK)

        val timeLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

        val taskLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 3f)

        newLine.addView(time, timeLayoutParams)
        newLine.addView(task, taskLayoutParams)

        newLine.setPadding(10, 10, 10, 10)

        return newLine
    }


    fun updateTaskList() {
        // Получить из базы данных список дел на день, выполнить сортировку
        addedTasks = ArrayList()

        var dbLocked = true
        val connection = DatabaseWorker()
        while (dbLocked) {
            try {
                connection.setConnection(this)
                dbLocked = false
            } catch (e: SQLiteDatabaseLockedException) {
                Thread.sleep(10)
                println("SLEEPING")
            }
        }

        println("GETTING ALL EVENTS FOR TODAY")
        val events = connection.readEventsForToday(currentDate.calendar.timeInMillis)
        for (event in events) {
            println("ADDING NEW EVENT")
            val newTask = Task()
            newTask.makeFromEvent(event)

            addToTaskList(newTask)
        }

        if (events.isEmpty()) {
            llMain.removeAllViews()
        }

        connection.closeConnection()
    }

    inner class TimeThread : Thread() {
        override fun run() {
            try {
                while (!isInterrupted) {
                    sleep(1000)
                    runOnUiThread(Runnable { currentDate.updateDate() })
                }
            } catch (e : InterruptedException) {
                println("Exception in TimeThread!")
            }
        }
    }
}
