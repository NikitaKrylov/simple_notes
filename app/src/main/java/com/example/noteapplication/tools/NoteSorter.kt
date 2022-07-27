package com.example.noteapplication.tools

import android.util.Log
import com.example.noteapplication.MainActivity
import com.example.noteapplication.model.Note

class NoteSorter {

    var currentSortType : Int = NO_SORT

    companion object {
        const val NO_SORT = 0
        const val BY_TAGS = 1
        const val BY_DATE = 2
        const val BY_TEXT_AMOUNT = 3

        fun sortByTextAmount(list: List<Note>) : List<Note>{
            return list.sortedWith(java.util.Comparator { note1, note2 ->
                when{
                    (note1.title.count() + note1.text.count() > note2.title.count() + note2.text.count()) -> 1
                    (note1.title.count() + note1.text.count() < note2.title.count() + note2.text.count()) -> -1
                    else -> 0
                }
            })
        }

        fun sortByDate(list: List<Note>): List<Note> {
            return list.sortedByDescending { it.creationDate.time }
        }

    }


    fun sort(list: List<Note>, sortType: Int?): List<Note>{

        if (sortType != null) currentSortType = sortType

        return when (currentSortType){
            BY_TEXT_AMOUNT -> sortByTextAmount(list)
            BY_DATE -> sortByDate(list)
            NO_SORT -> list
            else -> list
        }
    }


}