package com.example.noteapplication

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.noteapplication.databinding.ActivityTrashNoteBinding
import com.example.noteapplication.model.Note
import com.example.noteapplication.viewmodel.NoteViewModel

private const val EXTRA_TRASH_NOTE_ID = "com.noteapplication.trashnoteactivity.note_id"


class TrashNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrashNoteBinding
    private lateinit var mNoteViewModel : NoteViewModel
    private lateinit var currentNote: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // Support Action Bar
        setSupportActionBar(binding.trashNoteToolbar)
        supportActionBar?.title = "Trash Note"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.extras?.let{
            currentNote = mNoteViewModel.getById(it.getInt(EXTRA_TRASH_NOTE_ID))
            setDataToView(currentNote)
        } ?: run{
            throw Exception("id must be given into activity")
        }
    }

    private fun setDataToView(note: Note){
        note.let{
            Toast.makeText(this, it.deletingDateTime.toString(), Toast.LENGTH_LONG).show()
            binding.trashNoteText.text = it.text
            binding.trashNoteTitle.text = it.title
            binding.isFavouriteCheckBoxTrashNote.isChecked = it.isFavourite
            setBackgroundColor(getColor(it.backgroundColorId))
        }
    }

    private fun setBackgroundColor(color: Int){
        binding.trashNoteBackground.setBackgroundColor(color)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
        window.statusBarColor = color
        window.navigationBarColor = color
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.trash_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> onBackPressed()
            R.id.restore_note -> createRestoreDialog()
            R.id.delete_note_forever -> createDeleteForeverDialog()
        }
        return true
    }

    private fun createDeleteForeverDialog(){
        val builder = AlertDialog.Builder(this, R.style.Widget_Dialog_Alert).apply {
            setMessage(R.string.delete_note_forever_question)
            setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.cancel() }
            setPositiveButton("Delete"){ _, _ ->
                mNoteViewModel.delete(currentNote)
                val intent = Intent(applicationContext, TrashBoxActivity::class.java)
                startActivity(intent)
            }
        }
        builder.create().show()
    }

    private fun createRestoreDialog(){
        val builder = AlertDialog.Builder(this, R.style.Widget_Dialog_Alert).apply {
            setMessage(R.string.restore_note_question)
            setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.cancel() }
            setPositiveButton("Restore") { _, _ ->
                mNoteViewModel.restore(currentNote)
                val intent = Intent(applicationContext, NoteActivity::class.java).apply {
                    putExtra("NOTE_ID", currentNote.id)
                }
                startActivity(intent)
            }
        }
        builder.create().show()
    }

    companion object {
        fun getIntent(context: Context, noteId: Int) = Intent(context, TrashNoteActivity::class.java).apply {
            putExtra(EXTRA_TRASH_NOTE_ID, noteId)
        }
    }
}