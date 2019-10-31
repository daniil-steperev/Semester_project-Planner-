package com.example.planner

import android.content.ContentValues
import android.database.SQLException
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ognev.kotlin.agendacalendarview.CalendarController
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.builder.CalendarContentManager
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*
import kotlin.collections.ArrayList
import android.database.sqlite.SQLiteDatabase;
import com.example.planner.db.*
import java.io.IOException

class CalendarActivity : AppCompatActivity(), CalendarController {

    private var oldDate: Calendar? = null
    private var eventList: MutableList<CalendarEvent> = ArrayList()
    private lateinit var minDate: Calendar
    private lateinit var maxDate: Calendar
    private lateinit var contentManager: CalendarContentManager
    private var startMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var endMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private lateinit var mDBHelper: DatabaseHelper
    private lateinit var mDb: SQLiteDatabase

    private var loadingTask: LoadingTask? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        oldDate = Calendar.getInstance()
        minDate = Calendar.getInstance()
        maxDate = Calendar.getInstance()

        minDate.add(Calendar.MONTH, -10)
        minDate.add(Calendar.YEAR, -1)
        minDate.set(Calendar.DAY_OF_MONTH, 1)
        maxDate.add(Calendar.YEAR, 1)


        contentManager = CalendarContentManager(
            this,
            agenda_calendar_view,
            SampleEventAgendaAdapter(applicationContext)
        )

        contentManager.locale = Locale.ENGLISH
        contentManager.setDateRange(minDate, maxDate)

        //var connection = DatabaseConnection()
        //connection.setConnection(this)

        mDBHelper = DatabaseHelper(this)
        try {
            mDBHelper.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        }

        try {
            mDb = mDBHelper.writableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }

        /*var eventService = EventService()
        var event = Event()
        event.setName("Algebra")
        event.setDescription("Group theory")
        event.setTime(1367280000)*/
        //eventService.addEvent(event, mDb)
        var values = ContentValues()
        values.put("name", "hello")
        mDb.insert("Event", "null", values)

        val cursor = mDb.rawQuery("SELECT * FROM Event", null, null)
        cursor.moveToFirst()
        var result : String = ""
        while (!cursor.isAfterLast) {
            result += cursor.getString(1)
            cursor.moveToNext()
        }
        cursor.close()
        println("HAHA $result")

        mDb.close()
        /*cursor.moveToFirst()
        var i: Int = 0
        while (!cursor.isAfterLast) {
            println("WE ARE HERE")
            val day = Calendar.getInstance(Locale.ENGLISH)
            day.timeInMillis = cursor.getLong(3)
            eventList.add(
                MyCalendarEvent(
                    day, day,
                    DayItem.buildDayItemFromCal(day), SampleEvent(0, cursor.getString(2), "")
                ).setEventInstanceDay(day)
            )
            println(cursor.getString(2))
            cursor.moveToNext()
        }
        cursor.close()*/

        contentManager.loadItemsFromStart(eventList)
        agenda_calendar_view.agendaView.agendaListView.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            Toast.makeText(view.context, "item: ".plus(position), Toast.LENGTH_SHORT).show()}
    }

    override fun onStop() {
        super.onStop()
        loadingTask?.cancel(true)
    }

    override fun getEmptyEventLayout() = R.layout.view_agenda_empty_event

    override fun getEventLayout() = R.layout.view_agenda_event

    override fun onDaySelected(dayItem: IDayItem) {
    }

    override fun onScrollToDate(calendar: Calendar) {
        val lastPosition = agenda_calendar_view.agendaView.agendaListView.lastVisiblePosition + 1

        val isSameDay = oldDate?.isSameDay(calendar) ?: false
        if (isSameDay && lastPosition == CalendarManager.getInstance(this).events.size) {
            if (!agenda_calendar_view.isCalendarLoading()) { // condition to prevent asynchronous requests
                loadItemsAsync(false)
            }
        }

        if (agenda_calendar_view.agendaView.agendaListView.firstVisiblePosition == 0) {
            val minCal = Calendar.getInstance()
            minCal.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            if (calendar.get(Calendar.DAY_OF_MONTH) == minCal.get(Calendar.DAY_OF_MONTH)) {
                if (!agenda_calendar_view.isCalendarLoading()) { // condition to prevent asynchronous requests
                    loadItemsAsync(true)
                }
            }
        }

        oldDate = calendar
    }

    private fun loadItemsAsync(addFromStart: Boolean) {
        loadingTask?.cancel(true)

        loadingTask = LoadingTask(addFromStart)
        loadingTask?.execute()
    }

    inner class LoadingTask(private val addFromStart: Boolean) : AsyncTask<Unit, Unit, Unit>() {

        private val startMonthCal: Calendar = Calendar.getInstance()
        private val endMonthCal: Calendar = Calendar.getInstance()

        override fun onPreExecute() {
            super.onPreExecute()
            agenda_calendar_view.showProgress()
            eventList.clear()
        }

        override fun doInBackground(vararg params: Unit?) {
            Thread.sleep(2000) // simulating requesting json via rest api

            if (addFromStart) {
                if (startMonth == 0) {
                    startMonth = 11
                } else {
                    startMonth--
                }

                startMonthCal.set(Calendar.MONTH, startMonth)
                if (startMonth == 11) {
                    var year = startMonthCal.get(Calendar.YEAR)
                    startMonthCal.set(Calendar.YEAR, ++year)
                }


                for (i in 1..startMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    val day = Calendar.getInstance(Locale.ENGLISH)
                    day.timeInMillis = System.currentTimeMillis()
                    day.set(Calendar.MONTH, startMonth)
                    day.set(Calendar.DAY_OF_MONTH, i)
                    if (endMonth == 11) {
                        day.set(Calendar.YEAR, day.get(Calendar.YEAR) - 1)
                    }

                    eventList.add(
                        MyCalendarEvent(
                            day, day,
                            DayItem.buildDayItemFromCal(day),
                            SampleEvent(name = "Awesome $i", description = "Event $i")
                        )
                            .setEventInstanceDay(day)
                    )
                }
            } else {
                if (endMonth >= 11) {
                    endMonth = 0
                } else {
                    endMonth++
                }

                endMonthCal.set(Calendar.MONTH, endMonth)
                if (endMonth == 0) {
                    var year = endMonthCal.get(Calendar.YEAR)
                    endMonthCal.set(Calendar.YEAR, ++year)
                }

                for (i in 1..endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    val day = Calendar.getInstance(Locale.ENGLISH)
                    day.timeInMillis = System.currentTimeMillis()
                    day.set(Calendar.MONTH, endMonth)
                    day.set(Calendar.DAY_OF_MONTH, i)
                    if (endMonth == 0) {
                        day.set(Calendar.YEAR, day.get(Calendar.YEAR) + 1)
                    }

                    if (i % 4 == 0) {
                        val day1 = Calendar.getInstance()
                        day1.timeInMillis = System.currentTimeMillis()
                        day1.set(Calendar.MONTH, endMonth)
                        day1.set(Calendar.DAY_OF_MONTH, i)
                        eventList.add(
                            MyCalendarEvent(
                                day, day,
                                DayItem.buildDayItemFromCal(day),
                                SampleEvent(name = "Awesome $i", description = "Event $i")
                            )
                                .setEventInstanceDay(day)
                        )
                    }

                    eventList.add(
                        MyCalendarEvent(
                            day, day,
                            DayItem.buildDayItemFromCal(day),
                            SampleEvent(name = "Awesome $i", description = "Event $i")
                        )
                            .setEventInstanceDay(day)
                    )
                }
            }
        }

        override fun onPostExecute(user: Unit) {
            if (addFromStart) {
                contentManager.loadItemsFromStart(eventList)
            } else {
                contentManager.loadFromEndCalendar(eventList)
            }
            agenda_calendar_view.hideProgress()
        }
    }
}

