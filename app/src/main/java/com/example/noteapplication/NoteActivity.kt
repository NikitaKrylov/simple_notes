package com.example.noteapplication

import com.example.noteapplication.dock_instrument.ColorPickerFragment
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noteapplication.databinding.ActivityNoteBinding
import com.example.noteapplication.model.Note
import com.example.noteapplication.viewmodel.NoteViewModel
import java.util.*


//Страница чтения и редактирования заметки
class NoteActivity : AppCompatActivity() {

    private lateinit var mNoteViewModel : NoteViewModel
    private lateinit var titleInput : EditText
    private lateinit var textInput : EditText
    private var IS_SAVE_MOD : Boolean = false
    private var NOTE_ID : Int ?= null
    private lateinit var backgroundSurface : View
    private var currentNote : Note ?= null
    private lateinit var binding:ActivityNoteBinding
    private var destroyWithoutSaving = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.slide_to_top, R.anim.no_animation)
        setContentView(binding.root)

//      Support Action Bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

//      Views
        titleInput = findViewById(R.id.noteTitle)
        textInput = findViewById(R.id.noteText)
        backgroundSurface = findViewById(R.id.note_background)

        val arguments = intent.extras
        if (arguments != null) {
            IS_SAVE_MOD = arguments.getBoolean("IS_SAVE_MOD")
            if (IS_SAVE_MOD) {
                NOTE_ID = arguments.getInt("NOTE_ID")
                currentNote = mNoteViewModel.getById(NOTE_ID!!)
                setDataToView(currentNote!!)
            }
        } else {
            backgroundSurface.setBackgroundColor(getColor(R.color.default_note_background))
            supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.default_note_background)))
            setNavigationBarColor(this, getColor(R.color.default_note_background))
            window.statusBarColor = getColor(R.color.default_note_background)

        }
    }



    //Set up data into fields
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setDataToView(note:Note){
        ColorPickerFragment.setBackgroundColor(this, note.backgroundColorId)
        titleInput.setText(fromHtml(note.title))
        textInput.setText(fromHtml(note.text))
        backgroundSurface.setBackgroundColor(getColor(note.backgroundColorId))
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(note.backgroundColorId)))
        binding.isFavouriteCheckBox.isChecked = note.isFavourite
    }

    //Main save note function
    private fun saveNote(){
        val title = titleInput.text
        val text = textInput.text
        val isFavourite : Boolean = binding.isFavouriteCheckBox.isChecked

        var backgroundColorId : Int =  R.color.default_note_background
        if (ColorPickerFragment.backgroundColorId != null)
            backgroundColorId = ColorPickerFragment.backgroundColorId!!

        if (title.isEmpty() && text.isEmpty()) {
            currentNote?.let { mNoteViewModel.delete(it) }
            Toast.makeText(applicationContext, "Deleted successfully", Toast.LENGTH_SHORT)
        }
        else if (IS_SAVE_MOD){
            updateNote(NOTE_ID, title, text, backgroundColorId, isFavourite)
        }
        else{
            createNote(title, text, backgroundColorId, isFavourite)
        }
        ColorPickerFragment.backgroundColorId = null
    }


    private fun updateNote(noteId:Int?, title : Editable, text:Editable, backgroundColorId:Int, isFavourite:Boolean){
        if (noteId != null){
            currentNote = currentNote?.creationDate?.let {
                Note(noteId, title.toString(), text.toString(), backgroundColorId, it, isFavourite)
            }
            mNoteViewModel.update(currentNote!!)
        }
    }

    private fun createNote(title : Editable, text:Editable, backgroundColorId:Int, isFavourite:Boolean){
        currentNote = Note(0, title.toString(), text.toString(), backgroundColorId, Date(), isFavourite)
        mNoteViewModel.add(currentNote!!)
    }

    private fun deleteNote(note: Note){
        mNoteViewModel.delete(note)
        Toast.makeText(applicationContext, "Deleted successfully", Toast.LENGTH_SHORT)
        finish()
        overridePendingTransition(R.anim.slide_to_bottom, R.anim.no_animation)
    }

    private fun createDeleteDialog(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(this, R.style.Widget_Dialog_Alert)
        builder.apply {
            setMessage(resources.getString(R.string.delete_question))
            setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
                if (IS_SAVE_MOD) deleteNote(mNoteViewModel.getById(NOTE_ID!!))
                else
                    destroyWithoutSaving = true
                    finish()
                    overridePendingTransition(R.anim.no_animation, R.anim.slide_to_bottom)
            })
            setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, _ ->  dialogInterface.cancel()})

        }
        builder.create().show()
    }

    private fun createCalendarDialog(title:String, body:String){
        Toast.makeText(this, getString(R.string.not_implemented), Toast.LENGTH_LONG).show()
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
            R.id.delete_menu_btn -> {
                createDeleteDialog()
                return true
            }
            R.id.share_note_btn ->{
                intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, titleInput.text.toString() + "\n" + textInput.text.toString())
                    type = "text/plain"
                }
                startActivity(intent)
            }
            R.id.add_to_calendar -> {
                if (titleInput.text.isEmpty()) return true
                createCalendarDialog(titleInput.text.toString(), textInput.text.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (destroyWithoutSaving) return
        saveNote()
    }


    companion object {
        fun setNavigationBarColor(activity: Activity, color:Int) {
            activity.window.navigationBarColor = color
        }
        fun fromHtml(string:String) : Spanned{
            return HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

    }


}
