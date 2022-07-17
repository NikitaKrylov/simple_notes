package com.example.noteapplication

import android.content.*
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.adapter.NoteAdapter
import com.example.noteapplication.databinding.ActivityMainBinding
import com.example.noteapplication.model.Note
import com.example.noteapplication.viewmodel.NoteViewModel


// Страница приложения с выводом всех заметок
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter : NoteAdapter
    private lateinit var mNoteViewModel : NoteViewModel
    private lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

//        Support Action Bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = resources.getString(R.string.main_activity_title)

        binding.fab.setOnClickListener { _ ->
            intent = Intent(applicationContext, NoteActivity::class.java)
            startActivity(intent)
        }
        setNoteRecycler()
//        Register Receivers
//        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
//        val receiver = TrashboxCleanReciver()
//        registerReceiver(receiver, intentFilter)
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

        val searchItem = menu.findItem(R.id.search).actionView as SearchView
        searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(string: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(string: String?): Boolean {
                if (string.isNullOrEmpty()){
                    noteAdapter.setData(mNoteViewModel.getAll.value!!.toList())
                }
                else{
                    val list = mutableListOf<Note>()
                    mNoteViewModel.getAll.value!!.forEach{
                        if (it.title.lowercase().contains(string.lowercase()) || it.text.lowercase().contains(string.lowercase())) list.add(it)
                    }
                    noteAdapter.setData(list)
                }
                return true
            }

        })
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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.share_note_btn -> {
                intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, noteAdapter.getNote(item.groupId).title + "\n" + noteAdapter.getNote(item.groupId).text)
                    type = "text/plain"
                }
                startActivity(intent)
                return true
            }
            R.id.delete_menu_btn -> {
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.apply {
                    setMessage(resources.getString(R.string.delete_question))
                    setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
                        mNoteViewModel.delete(noteAdapter.getNote(item.groupId))
                        Snackbar.make(binding.fab, "Deleted successfully", Snackbar.LENGTH_LONG).show()
                    })
                    setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, _ ->  dialogInterface.cancel()})

                }
                builder.create().show()
                return true
            }
        }
        return super.onContextItemSelected(item)
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