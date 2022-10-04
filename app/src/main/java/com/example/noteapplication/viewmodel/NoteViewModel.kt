package com.example.noteapplication.viewmodel

import android.app.Application
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
    fun add(note: Note) = noteDao.add(note)
    fun delete(note: Note) = noteDao.delete(note)
    fun update(note:Note) = noteDao.update(note)
    fun getById(id:Int): Note = noteDao.getById(id)
    fun orderByDate(isAsc: Boolean = isAscSort): List<Note> = noteDao.orderByDate(isAsc)
    fun orderByIsFavourite(isAsc: Boolean): List<Note> = noteDao.orderByIsFavourite(isAsc)
    fun orderByTextAmount(isAsc: Boolean): List<Note> = noteDao.orderByTextAmount(isAsc)

    fun sort(sortType: SortType = currentSortTpe, isAsc: Boolean = isAscSort) : List<Note>{
        currentSortTpe = sortType
        isAscSort = isAsc
        return when (sortType){
            SortType.IsFavourite -> return orderByIsFavourite(isAsc)
            SortType.ByDate -> return orderByDate(isAsc)
            SortType.TextAmount -> return orderByTextAmount(isAsc)
        }
    }



}