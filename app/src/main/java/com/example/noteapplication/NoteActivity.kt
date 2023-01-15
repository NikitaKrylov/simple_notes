package com.example.noteapplication

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.CalendarContract
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.noteapplication.databinding.ActivityNoteBinding
import com.example.noteapplication.model.Note
import com.example.noteapplication.viewmodel.NoteViewModel
import java.util.*


//Страница чтения и редактирования заметки
class NoteActivity : AppCompatActivity() {

    private var IS_SAVE_MOD: Boolean = false
    private lateinit var mNoteViewModel : NoteViewModel
    private lateinit var titleInput : EditText
    private lateinit var textInput : EditText
    private lateinit var backgroundSurface : View
    private var currentNote : Note ?= null
    private lateinit var binding:ActivityNoteBinding
    private var destroyWithoutSaving = false

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
        arguments?.also {
            IS_SAVE_MOD = it.getBoolean("IS_SAVE_MOD", false)
            if (IS_SAVE_MOD) {
                currentNote = arguments.getInt("NOTE_ID").let { mNoteViewModel.getById(it) } ?: null
                setDataToView(currentNote!!)
            }
            else{
                setBackgroundColor(getColor(R.color.default_note_background))
            }
        } ?: {
            setBackgroundColor(getColor(R.color.default_note_background))
        }

    }


    private fun setDataToView(note:Note){
        titleInput.setText(note.title)
        textInput.setText(note.text)
        binding.isFavouriteCheckBox.isChecked = note.isFavourite
        setBackgroundColor(getColor(note.backgroundColorId))
    }

    private fun setBackgroundColor(color: Int){
        backgroundSurface.setBackgroundColor(color)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
        window.statusBarColor = color
        window.navigationBarColor = color
    }

    private fun updateNote(id: Int, title: String, text: String, color: Int, date: Date, isFavourite: Boolean){
        if (titleInput.text.isEmpty() && textInput.text.isEmpty()) {
            currentNote?.let {
                mNoteViewModel.putInTrash(it)
            }
        }
        else{
            mNoteViewModel.update(Note(id, title, text, color, date, isFavourite, currentNote?.inTrash ?: 0))
        }
    }

    private fun createNote(title: String, text: String, color: Int, isFavourite: Boolean){
        if (title.isNotEmpty() || text.isNotEmpty()) {
            currentNote = Note(0, title, text, color, Date(), isFavourite, 0)
            currentNote?.let{ mNoteViewModel.add(it) }
        }

    }

    private fun deleteNote(note: Note){
        mNoteViewModel.putInTrash(note)
    }

    private fun createDeleteDialog(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(this, R.style.Widget_Dialog_Alert)
        builder.apply {
            setMessage(resources.getString(R.string.delete_question))
            setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.cancel() }
            setPositiveButton("Delete") { _, _ ->

                if (IS_SAVE_MOD) {
                    currentNote?.let {
                        deleteNote(it)
                        destroyWithoutSaving = true
                    }
                }
                finish()
                overridePendingTransition(R.anim.no_animation, R.anim.slide_to_bottom)
            }

        }
        builder.create().show()
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
                if (titleInput.text.isEmpty() && textInput.text.isEmpty()) {
                    Toast.makeText(this, "Note is empty", Toast.LENGTH_LONG).show()
                }
                val intent = Intent(Intent.ACTION_EDIT).apply {
                    type = "vnd.android.cursor.item/event"
                    data = CalendarContract.Events.CONTENT_URI
                    if (titleInput.text.isNotEmpty()){
                        putExtra(CalendarContract.Events.TITLE, titleInput.text.toString())
                    }
                    if (textInput.text.isNotEmpty()){
                        putExtra(CalendarContract.Events.DESCRIPTION, textInput.text.toString())
                    }
                    putExtra(CalendarContract.Events.CALENDAR_COLOR, if (currentNote == null) currentNote?.backgroundColorId else R.color.default_note_background )
                }
                startActivity(intent)

            }
            R.id.page_color -> {
                Toast.makeText(this, "not implemented", Toast.LENGTH_LONG).show()
            }

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()

        Toast.makeText(this, currentNote.toString(), Toast.LENGTH_SHORT).show()

        if (destroyWithoutSaving) return
        if (IS_SAVE_MOD){
            currentNote?.apply {
                updateNote(
                    id,
                    titleInput.text.toString(),
                    textInput.text.toString(),
                    R.color.default_note_background,
                    creationDate,
                    binding.isFavouriteCheckBox.isChecked
                )
            }
        }
        else{
            createNote(
                titleInput.text.toString(),
                textInput.text.toString(),
                R.color.default_note_background,
                binding.isFavouriteCheckBox.isChecked
            )
        }
    }

}
