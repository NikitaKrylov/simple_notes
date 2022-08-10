package com.example.noteapplication.tools

import com.example.noteapplication.viewmodel.NoteViewModel
import kotlin.properties.Delegates


class NoteSaveManager {
    private lateinit var mNoteViewModel: NoteViewModel
    private var SAVE_MOD by Delegates.notNull<Boolean>()


    fun Delete(id: Int){

    }
    fun Save(){

    }

    private fun Create(){
    }
}