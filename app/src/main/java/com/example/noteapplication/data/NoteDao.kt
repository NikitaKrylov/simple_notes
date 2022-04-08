package com.example.noteapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noteapplication.model.Note

@Dao
interface NoteDao {

    @Insert
    fun add(note: Note)

    @Delete
    fun delete(note: Note)

    @Update
    fun update(note:Note)

    @Query("SELECT * FROM note_table ORDER BY id ASC")
    fun getAll(): LiveData<List<Note>>

    @Query("SELECT * FROM NOTE_TABLE WHERE id IN (:id)")
    fun getById(id:Int): Note
}