package com.example.planner

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.setPadding
import androidx.core.view.size
import com.example.planner.db.EventService
import com.example.planner.dialogs.CreateTaskDialog
import com.example.planner.dialogs.RemoveTaskDialog
import java.sql.Time

@Suppress("DEPRECATION")
class ToDoActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var llMain : LinearLayout
    private lateinit var addButton : Button
    private lateinit var deleteButton : Button

    private lateinit var addedTasks : ArrayList<Task>

    private lateinit var currentDate: CurrentDate
    private lateinit var timeThread : TimeThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo)

        llMain = findViewById(R.id.todo_window)
        addButton = findViewById(R.id.add_button)
        deleteButton = findViewById(R.id.delete_button)
        addedTasks = getAddedTasks()

        initializeDate()
        showAddedTasks()

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

    fun addToTaskList(newTask : Task) {
        val lParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        addedTasks.add(newTask)

        if (addedTasks.size != 0) {
            addedTasks.sort() // sort tasks
            if (llMain.size > 0) { // remove other tasks if they present
                llMain.removeAllViews()
            }

            for (task in addedTasks) { // add task in right order
                val newLine = getNewTaskLine(task)
                llMain.addView(newLine, lParams)
            }
        }
    }

    fun getNewTaskLine(task : Task) : LinearLayout {
        val timeText = task.getTime()
        val taskText = task.getTask()

        val newLine = LinearLayout(this)
        newLine.orientation = LinearLayout.HORIZONTAL

        val time = TextView(this)
        time.setPadding(5)
        time.text = timeText.toString()
        time.textSize = 20.toFloat()

        val task = TextView(this)
        task.setPadding(5)
        task.gravity = Gravity.CENTER
        task.text = taskText
        task.textSize = 20.toFloat()

        val timeLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

        val taskLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 3f)

        newLine.addView(time, timeLayoutParams)
        newLine.addView(task, taskLayoutParams)

        return newLine
    }

    private fun getAddedTasks() : ArrayList<Task> {
        val tasks = emptyArray<Task>()
        // Получить из базы данных список дел на день, выполнить сортировку

        tasks.sort()
        return arrayListOf()
    }

    /** A method that show all added tasks for today.
     *
     *  This method will be useful when you start the activity
     * */
    fun showAddedTasks() {
        val connection = DatabaseWorker()
        connection.setConnection(this)
        val eventService = EventService()

        val events = eventService.getAllEventsForToday(connection.getmDb(), currentDate.calendar)
        for (event in events) {
            val newTask = Task()
            newTask.makeFromEvent(event)

            addToTaskList(newTask)
        }
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
