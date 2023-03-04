package com.example.noteapplication.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.noteapplication.data.NoteDatabase
import com.example.noteapplication.model.Note

enum class SortType{
    ByDate, IsFavourite, TextAmount
}

enum class SortDirection{
    ASC, DESC
}

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDao = NoteDatabase.getDatabase(application).noteDao()
    private var sortDirection: SortDirection = SortDirection.ASC

    val getAll: LiveData<List<Note>> = noteDao.getAll()
    val getTrashAll: LiveData<List<Note>> = noteDao.getTrashAll()
    fun add(note: Note) = noteDao.add(note)
    fun delete(note: Note) = noteDao.delete(note)
    fun putInTrash(note: Note) = noteDao.putInTrash(note.id)
    fun restoreAll() = noteDao.restoreAllFromTrash()
    fun deleteAllForever() = noteDao.deleteAllForever()
    fun restore(note: Note) = noteDao.restore(note.id)
    fun update(note:Note) = noteDao.update(note)
    fun getById(id:Int): Note = noteDao.getById(id)

}