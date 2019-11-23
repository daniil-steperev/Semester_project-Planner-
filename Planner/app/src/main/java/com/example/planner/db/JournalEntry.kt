package com.example.planner.db

class JournalEntry() {
    private var id : Long = 0
    private lateinit var name : String
    private lateinit var description : String
    private var time : Long = 0
    private var successful : Boolean = false

    fun setDescription(result : String) {
        description = result
    }

    fun getDescription() : String {
        return description
    }

    fun setDoneSuccessful(result : Int) {
        if (result == 0) {
            successful = false
            return
        }
        successful = true
    }

    fun getDoneSuccessful() : Boolean {
        return successful
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