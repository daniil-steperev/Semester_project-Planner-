package com.example.planner

import com.example.planner.db.Trigger
import com.example.planner.db.TriggerRule
import org.junit.Assert.*
import org.junit.Test

class TriggerTest {
    @Test
    fun dayOfTheWeekTest() {
        val triggerMonday =
            Trigger(1, TriggerRule.MONDAY)
        val triggerTuesday =
            Trigger(2, TriggerRule.TUESDAY)
        val triggerWednesday =
            Trigger(3, TriggerRule.WEDNESDAY)
        val triggerThursday =
            Trigger(4, TriggerRule.THURSDAY)
        val triggerFriday =
            Trigger(5, TriggerRule.FRIDAY)
        val triggerSaturday =
            Trigger(6, TriggerRule.SATURDAY)
        val triggerSunday =
            Trigger(7, TriggerRule.SUNDAY)

        val triggerDaily = Trigger(8, TriggerRule.DAILY)

        val currentDate : Long =  1572460589937 // System.currentTimeMillis() Wednesday
        println(currentDate)

        assertFalse(triggerMonday.suits(currentDate))
        assertFalse(triggerTuesday.suits(currentDate))
        assertFalse(triggerThursday.suits(currentDate))
        assertFalse(triggerFriday.suits(currentDate))
        assertFalse(triggerSaturday.suits(currentDate))
        assertFalse(triggerSunday.suits(currentDate))

        assertTrue(triggerWednesday.suits(currentDate))
        assertTrue(triggerDaily.suits(currentDate))
    }
}