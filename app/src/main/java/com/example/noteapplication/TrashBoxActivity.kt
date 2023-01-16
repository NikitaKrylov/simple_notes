package com.example.noteapplication

import android.content.Intent
import android.content.res.Configuration
import android.media.MediaSession2Service
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.adapter.TrashNoteAdapter
import com.example.noteapplication.databinding.ActivityTrashBoxBinding
import com.example.noteapplication.model.Note
import com.example.noteapplication.viewmodel.NoteViewModel

class TrashBoxActivity : AppCompatActivity() {
    private lateinit var noteAdapter: TrashNoteAdapter
    private lateinit var mNoteViewModel: NoteViewModel
    private lateinit var binding: ActivityTrashBoxBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashBoxBinding.inflate(layoutInflater)
        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        setContentView(binding.root)

        setSupportActionBar(binding.trashBoxToolbar)
        supportActionBar?.title = resources.getString(R.string.trashbox)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        setTrashNoteRecycler()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> onBackPressed()
            R.id.delete_all_forever -> createDeleteForeverDialog()
            R.id.restore_all -> createRestoreDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createDeleteForeverDialog(){
        val builder = AlertDialog.Builder(this, R.style.Widget_Dialog_Alert).apply {
            setMessage(R.string.delete_all_forever_question)
            setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.cancel() }
            setPositiveButton("Delete all"){ _, _ ->
                mNoteViewModel.deleteAllForever()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        }
        builder.create().show()
    }

    private fun createRestoreDialog(){
        val builder = AlertDialog.Builder(this, R.style.Widget_Dialog_Alert).apply {
            setMessage(R.string.restore_all_question)
            setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.cancel() }
            setPositiveButton("Restore all") { _, _ ->
                mNoteViewModel.restoreAllFromTrash()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        }
        builder.create().show()
    }

    private fun setTrashNoteRecycler(){
        noteAdapter = TrashNoteAdapter(this )
        mNoteViewModel.getTrashAll.observe(this) { notes ->
            noteAdapter.setData(notes)
            binding.trashNoteAmountText.text = "${getString(R.string.trash_note_amount_text)} ${notes.count()}"
        }
        findViewById<RecyclerView>(R.id.trashNoteRecycler).apply {
            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                GridLayoutManager(applicationContext, 2, GridLayoutManager.VERTICAL, false) }
            else{
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false) }
            adapter = noteAdapter
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_trash_box, menu)
        return true
    }

}