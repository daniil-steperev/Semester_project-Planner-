package com.example.planner.db

import android.database.Cursor

class TriggerService {
    // FIXME: SELECT OFFER (запрос)

    fun readTrigger(date : Long) : MutableList<Trigger> {
        // FIXME
        return mutableListOf()
    }

    fun map(cursor : Cursor) : Trigger {
        // FIXME
        return Trigger(1, TriggerRule.MONDAY)
    }
}