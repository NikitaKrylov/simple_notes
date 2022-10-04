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

    @Query("""SELECT * FROM note_table ORDER BY
            CASE WHEN :isAsc = 1 THEN creationDate END ASC,
            CASE WHEN :isAsc = 0 THEN creationDate END DESC""")
    fun orderByDate(isAsc: Boolean): List<Note>


    @Query("""SELECT * FROM note_table ORDER BY
            CASE WHEN :isAsc = 1 THEN isFavourite END ASC,
            CASE WHEN :isAsc = 0 THEN isFavourite END DESC,
            creationDate DESC""")
    fun orderByIsFavourite(isAsc: Boolean): List<Note>

    @Query("""SELECT * FROM note_table ORDER BY
            CASE WHEN :isAsc = 1 THEN LENGTH(text) + LENGTH(title) END ASC,
            CASE WHEN :isAsc = 0 THEN LENGTH(text) + LENGTH(title) END DESC""")
    fun orderByTextAmount(isAsc: Boolean): List<Note>
}