package com.example.noteapplication

import android.content.DialogInterface
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

    private lateinit var mNoteViewModel : NoteViewModel
    private lateinit var titleInput : EditText
    private lateinit var textInput : EditText
    private var IS_SAVE_MOD : Boolean = false
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
        if (arguments != null) {
            IS_SAVE_MOD = arguments.getBoolean("IS_SAVE_MOD")
            if (IS_SAVE_MOD) {
                currentNote = arguments.getInt("NOTE_ID").let { mNoteViewModel.getById(it) } ?: null
                setDataToView(currentNote!!)
            }
        } else {
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
                mNoteViewModel.delete(it)
                Toast.makeText(applicationContext, "Deleted successfully", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            mNoteViewModel.update(Note(id, title, text, color, date, isFavourite))
        }
    }

    private fun createNote(title: String, text: String, color: Int, isFavourite: Boolean){
        if (title.isNotEmpty() || text.isNotEmpty()) {
            currentNote = Note(0, title, text, color, Date(), isFavourite)
            currentNote?.let{ mNoteViewModel.add(it) }
        }

    }

    private fun deleteNote(note: Note){
        mNoteViewModel.delete(note)
        Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT)
    }

    private fun createDeleteDialog(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(this, R.style.Widget_Dialog_Alert)
        builder.apply {
            setMessage(resources.getString(R.string.delete_question))
            setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, _ ->  dialogInterface.cancel()})
            setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->
                if (IS_SAVE_MOD) {
                    currentNote?.let{ mNoteViewModel.delete(it) }
                    finish()
                    overridePendingTransition(R.anim.slide_to_bottom, R.anim.no_animation)
                }
                else
                    finish()
                    overridePendingTransition(R.anim.no_animation, R.anim.slide_to_bottom)
            })

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
                if (titleInput.text.isEmpty() && textInput.text.isEmpty()) {
                    Toast.makeText(this, "111", Toast.LENGTH_LONG).show()
                    return true
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
