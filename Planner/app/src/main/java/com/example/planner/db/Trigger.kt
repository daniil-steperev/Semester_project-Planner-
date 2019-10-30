package com.example.planner.db

class Trigger {
    private var id : Long = 0
    private var rule : String = ""

    constructor(id : Long, rule : String) {
        this.id = id
        this.rule = rule
    }

    fun getRule() : String {
        return rule
    }

    fun suits(date : Long) : Boolean {
        return true // FIXME
    }
}