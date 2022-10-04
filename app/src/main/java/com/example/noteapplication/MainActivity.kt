package com.example.noteapplication

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.adapter.NoteAdapter
import com.example.noteapplication.databinding.ActivityMainBinding
import com.example.noteapplication.model.Note
import com.example.noteapplication.tools.NoteSorter
import com.example.noteapplication.viewmodel.NoteViewModel
import com.example.noteapplication.viewmodel.SortType
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


// Страница приложения с выводом всех заметок
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter : NoteAdapter
    private lateinit var mNoteViewModel : NoteViewModel
    private lateinit var preferences : SharedPreferences
    private var mNoteSorter = NoteSorter()

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

        val toggle = ActionBarDrawerToggle(this, binding.navigationDrawer, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.navigationDrawer.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        binding.fab.setOnClickListener { _ ->
            intent = Intent(applicationContext, NoteActivity::class.java)
            startActivity(intent)
        }
        setNoteRecycler()

    }


    override fun onResume() {
        super.onResume()
        setApplicationTheme(preferences.getString("theme_list_preferences", "Light"))

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
        item.isChecked = item.isChecked

        return when (item.itemId) {
            R.id.order_by_date -> {
                noteAdapter.setData(mNoteViewModel.sort(SortType.ByDate))
                return true
            }

            R.id.order_by_is_favourite -> {
                noteAdapter.setData(mNoteViewModel.sort(SortType.IsFavourite))
                return true
            }
            R.id.order_by_text_amount ->{
                noteAdapter.setData(mNoteViewModel.sort(SortType.TextAmount))
                return true
            }
            R.id.order_direction -> {
                mNoteViewModel.isAscSort = !mNoteViewModel.isAscSort
                if (mNoteViewModel.isAscSort) item.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_upward)
                else  item.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_downward)
                noteAdapter.setData(mNoteViewModel.sort())

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
                    val currentNote = noteAdapter.getNote(noteAdapter.getPosition())
                    putExtra(Intent.EXTRA_TEXT, currentNote.title + "\n" + currentNote.text)
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
                        mNoteViewModel.delete(noteAdapter.getNote(noteAdapter.getPosition()))
                        Snackbar.make(binding.fab, "Deleted successfully", Snackbar.LENGTH_LONG).show()
                    })
                    setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, _ ->  dialogInterface.cancel()})

                }
                builder.create().show()
                return true
            }
            R.id.isFavouriteCheckBox -> {
                val note = noteAdapter.getNote()
                note.isFavourite = !note.isFavourite
                mNoteViewModel.update(note)
                noteAdapter.notifyItemChanged(item.groupId)
                return true
            }


        }
        return super.onContextItemSelected(item)
    }


    private fun setNoteRecycler(){
        noteAdapter = NoteAdapter(this )
        mNoteViewModel.getAll.observe(this, Observer { notes ->
            noteAdapter.setData(mNoteViewModel.sort())
            binding.noteAmountText.text = getString(R.string.note_amount_text) + " " + notes.count()
        })

        val recyclerNote = findViewById<RecyclerView>(R.id.noteRecycler)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 2, GridLayoutManager.VERTICAL, false)
            recyclerNote.layoutManager = layoutManager
        }
        else{
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            recyclerNote.layoutManager = layoutManager
        }

        recyclerNote.adapter = noteAdapter
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_trashbox -> {
                Intent(this, TrashBoxActivity::class.java).also {
                    startActivity(it)
                }
                return true
            }
        }
        return false
    }

    companion object{
        const val LOG_TAG = "MY_LOG"
    }
}