package com.example.noteapplication

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.adapter.NoteAdapter
import com.example.noteapplication.viewmodel.NoteViewModel
import com.google.android.material.appbar.MaterialToolbar

class TrashBoxActivity : AppCompatActivity() {
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var mNoteViewModel: NoteViewModel
    private lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash_box)

        toolbar = findViewById(R.id.collapsing_toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.trashbox)

        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        setNoteRecycler()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setNoteRecycler(){
        noteAdapter = NoteAdapter(this )
        mNoteViewModel.getTrashAll.observe(this) { notes ->
            noteAdapter.setData(notes)
        }

        findViewById<RecyclerView>(R.id.noteRecycler).apply {
            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                GridLayoutManager(applicationContext, 2, GridLayoutManager.VERTICAL, false) }
            else{
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false) }
            adapter = noteAdapter
        }


    }

}