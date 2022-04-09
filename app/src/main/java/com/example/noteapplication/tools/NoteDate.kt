package com.example.noteapplication.tools
import java.text.SimpleDateFormat
import java.util.*

class NoteDate {
    private val date = Date()
    private val df = SimpleDateFormat("MMM d, HH:mm")

    override fun toString(): String {
        return df.format(date)
    }

    fun parse(str:String):Date? = df.parse(str)

}