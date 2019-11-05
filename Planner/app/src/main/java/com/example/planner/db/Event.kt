package com.example.planner.db

class Event {
    private var id : Long = 0
    private lateinit var name: String
    private lateinit var description : String
    private var time : Long = 0
    private var doneSuccessful : Boolean = false

    constructor()

    fun setDescription(result : String) {
        description = result
    }

    fun getDescription() : String {
        return description
    }

    fun setDoneSuccessful(result : Boolean) {
        doneSuccessful = result
    }

    fun getDoneSuccessful() : Boolean {
        return doneSuccessful
    }

    fun setTime(newTime : Long) {
        time = newTime
    }

    fun getTime() : Long {
        return time
    }

    fun setID(newId : Long) {
        id =  newId
    }

    fun getID() : Long {
        return id;
    }

    fun setName(newName : String) {
        name = newName
    }

    fun getName() : String {
        return name
    }
}