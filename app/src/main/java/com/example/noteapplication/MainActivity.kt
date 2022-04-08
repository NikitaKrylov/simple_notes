package com.example.noteapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.adapter.NoteAdapter
import com.example.noteapplication.databinding.ActivityMainBinding
import com.example.noteapplication.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter : NoteAdapter
    private lateinit var mNoteViewModel : NoteViewModel
    private lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        setApplicationTheme(preferences.getString("theme_list_preferences", "Dark"))

        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.fab.setOnClickListener { _ ->
            intent = Intent(applicationContext, NoteActivity::class.java)
            startActivity(intent)
        }
        setNoteRecycler()

    }

    override fun onResume() {
        super.onResume()
        setApplicationTheme(preferences.getString("theme_list_preferences", "Dark"))
    }

    private fun setApplicationTheme(string: String?) {
        when (string){
            "Light" -> { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "System" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    private fun setNoteRecycler(){
        noteAdapter = NoteAdapter(this )
        mNoteViewModel.getAll.observe(this, Observer { notes ->
            noteAdapter.setData(notes)
        })

        val recyclerNote = findViewById<RecyclerView>(R.id.noteRecycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        recyclerNote.layoutManager = layoutManager
        recyclerNote.adapter = noteAdapter
    }

    companion object{
        const val LOG_TAG = "MY_LOG"
    }

}