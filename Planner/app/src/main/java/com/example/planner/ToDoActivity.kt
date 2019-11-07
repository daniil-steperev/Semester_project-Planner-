package com.example.planner

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.core.view.setPadding
import androidx.core.view.size
import com.example.planner.dialogs.CreateTaskDialog
import java.sql.Time
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class ToDoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var llMain : LinearLayout
    private lateinit var addButton : Button
    private lateinit var deleteButton : Button
    private lateinit var addedTasks : ArrayList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo)

        llMain = findViewById(R.id.todo_window)
        addButton = findViewById(R.id.add_button)
        deleteButton = findViewById(R.id.delete_button)
        addedTasks = getAddedTasks()

        addButton.setOnClickListener(this)
        deleteButton.setOnClickListener(this)
    }

    @SuppressLint("ResourceType")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_button -> {
                val createTaskDialog = CreateTaskDialog(this)
                createTaskDialog.show(supportFragmentManager, "createTaskDialog")
            }

            R.id.delete_button -> {
                if (llMain.size > 0) {
                    llMain.removeView(llMain.get(llMain.size - 1))
                    addedTasks.remove(addedTasks.get(addedTasks.lastIndex))
                }
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
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
                val newLine = getNewTaskLine(task.getTime(), task.getTask())
                llMain.addView(newLine, lParams)
            }
        }
    }

    private fun getNewTaskLine(timeText : Time, taskText : String) : LinearLayout {
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
}
