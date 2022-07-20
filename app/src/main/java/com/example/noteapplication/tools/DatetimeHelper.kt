package com.example.noteapplication.tools

import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DatetimeHelper {

    companion object {
        const val pattern: String = "MMM d, HH:mm"
        val formatter = DateTimeFormatter.ISO_OFFSET_TIME
        fun now(): String {
            return LocalDate.now().format(formatter)
        }
    }

}