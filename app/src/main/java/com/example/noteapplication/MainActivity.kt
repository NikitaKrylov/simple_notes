package com.example.noteapplication

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.adapter.NoteAdapter
import com.example.noteapplication.databinding.ActivityMainBinding
import com.example.noteapplication.model.Note
import com.example.noteapplication.viewmodel.NoteViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

const val TAG = ".MainActivity"

// Страница приложения с выводом всех заметок
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter : NoteAdapter
    private lateinit var mNoteViewModel : NoteViewModel
    private lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        setApplicationTheme(preferences.getString("theme_list_preferences", "Light"))
        setContentView(binding.root)
        binding.navigationView.setNavigationItemSelectedListener(this)

//        Support Action Bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = resources.getString(R.string.main_activity_title)

        ActionBarDrawerToggle(this, binding.navigationDrawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close).also {
            binding.navigationDrawer.addDrawerListener(it)
            it.isDrawerIndicatorEnabled = true
            it.syncState()
        }

        binding.fab.setOnClickListener {
            intent = NoteActivity.getIntent(this)
            startActivity(intent)
        }
        setNoteRecycler()

    }


    override fun onResume() {
        super.onResume()
        setApplicationTheme(preferences.getString("theme_list_preferences", "Light"))
    }

    override fun onStop() {
        super.onStop()
        binding.navigationDrawer.closeDrawer(Gravity.START)
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
            override fun onQueryTextSubmit(string: String?): Boolean = true

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
        item.isChecked = item.isChecked

//        when (item.itemId) {
//            R.id.order_by_date -> noteAdapter.setData(mNoteViewModel.sort(SortType.ByDate))
//
//            R.id.order_by_is_favourite -> noteAdapter.setData(mNoteViewModel.sort(SortType.IsFavourite))
//
//            R.id.order_by_text_amount -> noteAdapter.setData(mNoteViewModel.sort(SortType.TextAmount))
//
//            R.id.order_direction -> {
//                mNoteViewModel.isAscSort = !mNoteViewModel.isAscSort
//                if (mNoteViewModel.isAscSort) item.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_upward)
//                else  item.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_downward)
//                noteAdapter.setData(mNoteViewModel.sort())
//            }
//        }
        return true
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val selectedNote = noteAdapter.getNote(noteAdapter.getPosition())

        when (item.itemId){
            R.id.share_note_btn -> {
                intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    selectedNote.also {
                        putExtra(Intent.EXTRA_TEXT, it.title + "\n" + it.text)
                    }
                    type = "text/plain"
                }
                startActivity(intent)
            }
            R.id.delete_menu_btn -> {
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.apply {
                    setMessage(resources.getString(R.string.delete_question))
                    setPositiveButton("Ok") { _, _ ->
                        mNoteViewModel.putInTrash(selectedNote)
                        Snackbar.make(binding.fab, "Deleted successfully", Snackbar.LENGTH_LONG).show()
                    }
                    setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.cancel() }

                }
                builder.create().show()
            }
            R.id.isFavouriteCheckBox -> {
                noteAdapter.getNote().also {
                    it.isFavourite = !it.isFavourite
                    mNoteViewModel.update(it)
                }
                noteAdapter.notifyItemChanged(item.groupId)
            }

        }
        return super.onContextItemSelected(item)
    }


    private fun setNoteRecycler(){
        noteAdapter = NoteAdapter(this )
        mNoteViewModel.getAll.observe(this) { notes ->
            noteAdapter.setData(notes)
            binding.noteAmountText.text = "${getString(R.string.note_amount_text)} ${notes.count()}"
        }
        findViewById<RecyclerView>(R.id.noteRecycler).apply {
            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                GridLayoutManager(applicationContext, 2, GridLayoutManager.VERTICAL, false) }
            else{
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false) }
            adapter = noteAdapter
        }


    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.action_trashbox -> {
                Intent(this, TrashBoxActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return true
    }

}