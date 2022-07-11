package com.example.noteapplication.tools

import com.example.noteapplication.viewmodel.NoteViewModel
import kotlin.properties.Delegates


//val title:String,
//val text:String,
//val backgroundColorId:Int,
//val creationDatetime:String,
//val isFavourite: Int = 0

class NoteSaveManager {
    private lateinit var mNoteViewModel: NoteViewModel
    private var SAVE_MOD by Delegates.notNull<Boolean>()


    public fun Delete(id: Int){

    }
    public fun Save(){

    }

    private fun Create(){
    }
}