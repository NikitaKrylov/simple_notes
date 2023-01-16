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

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDao = NoteDatabase.getDatabase(application).noteDao()
    var isAscSort: Boolean = false
    var currentSortTpe: SortType = SortType.ByDate

    val getAll: LiveData<List<Note>> = noteDao.getAll()
    val getTrashAll: LiveData<List<Note>> = noteDao.getTrashAll()
    fun add(note: Note) = noteDao.add(note)
    fun delete(note: Note) = noteDao.delete(note)
    fun putInTrash(note: Note) = noteDao.putInTrash(note.id)
    fun restoreAllFromTrash() = noteDao.restoreAllFromTrash()
    fun deleteAllForever() = noteDao.deleteAllForever()
    fun restore(note: Note) = noteDao.restore(note.id)

    fun update(note:Note) = noteDao.update(note)
    fun getById(id:Int): Note = noteDao.getById(id)
    fun orderByDate(isAsc: Boolean = isAscSort): List<Note> = noteDao.orderByDate(isAsc)
    fun orderByIsFavourite(isAsc: Boolean): List<Note> = noteDao.orderByIsFavourite(isAsc)
    fun orderByTextAmount(isAsc: Boolean): List<Note> = noteDao.orderByTextAmount(isAsc)

    fun sort(sortType: SortType = currentSortTpe, isAsc: Boolean = isAscSort) : List<Note>{
        currentSortTpe = sortType
        isAscSort = isAsc
        return when (sortType){
            SortType.IsFavourite -> orderByIsFavourite(isAsc)
            SortType.ByDate -> orderByDate(isAsc)
            SortType.TextAmount -> orderByTextAmount(isAsc)
        }
    }



}