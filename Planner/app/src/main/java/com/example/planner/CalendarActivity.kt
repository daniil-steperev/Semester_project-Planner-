package com.example.planner

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.planner.calendar.MyCalendarEvent
import com.example.planner.calendar.SampleEvent
import com.example.planner.calendar.SampleEventAgendaAdapter
import com.example.planner.calendar.isSameDay
import com.example.planner.db.*
import com.example.planner.gestures.BaseSwipeToDismissActivity
import com.ognev.kotlin.agendacalendarview.CalendarController
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.builder.CalendarContentManager
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import kotlinx.android.synthetic.main.activity_calendar.*

import java.util.*
import kotlin.collections.ArrayList

class CalendarActivity : BaseSwipeToDismissActivity(), CalendarController {

    private var oldDate: Calendar? = null
    private var eventList: MutableList<CalendarEvent> = ArrayList()
    private lateinit var minDate: Calendar
    private lateinit var maxDate: Calendar
    private lateinit var contentManager: CalendarContentManager
    private var startMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var endMonth: Int = Calendar.getInstance().get(Calendar.MONTH)

    private var loadingTask: LoadingTask? = null

    override fun getLayoutId(): Int {
        return R.layout.statistics
    }

    override fun isActivityDraggable(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        oldDate = Calendar.getInstance()
        minDate = Calendar.getInstance()
        maxDate = Calendar.getInstance()

        minDate.add(Calendar.MONTH, -2)
        minDate.add(Calendar.YEAR, -1)
        minDate.set(Calendar.DAY_OF_MONTH, 1)
        maxDate.add(Calendar.YEAR, 1)


        contentManager = CalendarContentManager(this, agenda_calendar_view,
            SampleEventAgendaAdapter(applicationContext)
        )

        contentManager.locale = Locale.ENGLISH
        contentManager.setDateRange(minDate, maxDate)

        var connection = DatabaseWorker()
        connection.setConnection(this)

        var event = Event()
        event.setName("English")
        event.setDescription("Problem-solution essay")
        event.setTime(1573145514481)

        var chosenTriggers : MutableList<Trigger> = LinkedList<Trigger>()
        chosenTriggers.add(Trigger(4, TriggerRule.THURSDAY))
        //trigger = "MONDAY"
        connection.addEvent(event, chosenTriggers)

        /*var date1 : Date = Date(minDate.timeInMillis)
        println("mindate" + minDate.timeInMillis + " " + date1.year + " " + date1.month + " " + date1.day )
        var date2 : Date = Date(maxDate.timeInMillis)
        println("maxdate" + " " + minDate.timeInMillis + " " + date2.year + " " + date2.month + " " + date2.day )*/

        val day = Calendar.getInstance()

        var initDay = minDate.timeInMillis
        var count : Int = 0
        while (initDay < maxDate.timeInMillis) {
            var list : List<Event> =  connection.readEventsForToday(initDay)
            val day = Calendar.getInstance()
            //count++
            for (i in list) {
                val day = Calendar.getInstance(Locale.ENGLISH)
                if (i.getTime() <= initDay) {
                    day.timeInMillis = initDay
                    eventList.add(
                        MyCalendarEvent(
                            day,
                            day,
                            DayItem.buildDayItemFromCal(day),
                            SampleEvent(
                                0,
                                name = i.getName(),
                                description = i.getDescription()
                            )
                        ).setEventInstanceDay(day)
                    )
                }
            }
            initDay += 86400000
        }
        /*println("Events actual for 14 november")
        var list : List<Event> =  connection.readEventsForToday(1573678800000)
        for (i in list) {
            println(i.getName() + " " + i.getTime() + " " + i.getDescription())
        }

        val day = Calendar.getInstance()


        for (i in list) {
            val day = Calendar.getInstance(Locale.ENGLISH)
            day.timeInMillis = i.getTime()
            eventList.add(MyCalendarEvent(day, day,
                DayItem.buildDayItemFromCal(day), SampleEvent(0, name = i.getName(), description = i.getDescription())).setEventInstanceDay(day))
        }*/

        connection.closeConnection()
        connection.getmDb().close()


        val maxLength = Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..maxLength) {
            val day = Calendar.getInstance(Locale.ENGLISH)
            day.timeInMillis = System.currentTimeMillis()
            day.set(Calendar.DAY_OF_MONTH, i)

            eventList.add(
                MyCalendarEvent(
                    day, day,
                    DayItem.buildDayItemFromCal(day), null
                ).setEventInstanceDay(day))
        }
        contentManager.loadItemsFromStart(eventList)
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
        println("Scroll")
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
            Thread.sleep(20) // simulating requesting json via rest api

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