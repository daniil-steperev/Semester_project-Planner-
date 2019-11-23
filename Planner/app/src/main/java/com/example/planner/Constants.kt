package com.example.planner

class Constants(newLastEntryDate : Long) {
    private var lastEntryDate : Long = newLastEntryDate

    fun getLastEntryDate() : Long {
        return lastEntryDate
    }

    fun setLastEntryDate(newDate : Long) {
        lastEntryDate = newDate
    }
}