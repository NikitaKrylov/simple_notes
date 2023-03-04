package com.example.noteapplication.tools.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateTimeConverter {


    @TypeConverter
    fun toDate(dateLong: Long) : Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long{
        return date?.time ?: 0.toLong()
    }



}