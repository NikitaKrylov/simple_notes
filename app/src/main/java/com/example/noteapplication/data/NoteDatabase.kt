package com.example.noteapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.noteapplication.model.Note
import com.example.noteapplication.tools.converter.DateTimeConverter

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao():NoteDao

    companion object{

        @Volatile
        private var INSTANCE:NoteDatabase ?= null

        fun getDatabase(context: Context):NoteDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){return tempInstance}


            val instance = Room.databaseBuilder(context, NoteDatabase::class.java, "note_data").allowMainThreadQueries().build()
            INSTANCE = instance
            return instance

        }
    }

}