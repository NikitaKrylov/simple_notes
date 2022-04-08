package com.example.noteapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.noteapplication.data.NoteDatabase
import com.example.noteapplication.model.Note

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDao = NoteDatabase.getDatabase(application).noteDao()

    public val getAll: LiveData<List<Note>> = noteDao.getAll()
    fun add(note: Note) = noteDao.add(note)
    fun delete(note: Note) = noteDao.delete(note)
    fun update(note:Note) = noteDao.update(note)
    fun getById(id:Int): Note = noteDao.getById(id)
}