package com.example.noteapplication.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.MenuCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.MainActivity
import com.example.noteapplication.NoteActivity
import com.example.noteapplication.R
import com.example.noteapplication.model.Note
import java.text.SimpleDateFormat
import kotlin.coroutines.coroutineContext

class NoteAdapter(var context: Context) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes = emptyList<Note>()
    private var preferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var position: Int = 0;

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val noteView = LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false)
        return NoteAdapter.NoteViewHolder(noteView, context)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note =  notes[position]


        if (NoteActivity.fromHtml(note.text).isEmpty()){
            holder.text.visibility = View.GONE
        }
        else{
            holder.text.visibility = View.VISIBLE
            holder.text.maxLines = preferences.getInt("max_note_card_line_preferences", R.integer.default_max_note_card_line_count)
            holder.text.text = NoteActivity.fromHtml(note.text)
        }

        holder.title.text = NoteActivity.fromHtml(note.title)
        holder.date.text = SimpleDateFormat("d MMM HH:mm").format(note.creationDate)
        holder.favouriteMark.visibility = when (note.isFavourite) {true -> ImageView.VISIBLE else -> ImageView.GONE}


        val color = context.getColor(note.backgroundColorId)
        holder.cardItem.setCardBackgroundColor(color)

        holder.cardItem.setOnClickListener {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra("IS_SAVE_MOD", true)
            intent.putExtra("NOTE_ID", note.id)

            startActivity(context, intent, null )

        }
        holder.cardItem.setOnLongClickListener {
            setPosition(position)
            false
        }



    }

    fun getNote(position: Int = this.position): Note{
        return notes[position]
    }

    override fun getItemCount(): Int {
        return notes.size
    }
    fun setPosition(position: Int){
        this.position = position
    }
    fun getPosition(): Int {
        return position
    }


    @RequiresApi(Build.VERSION_CODES.M)
    class NoteViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{
        val title: TextView = itemView.findViewById(R.id.note_card_title)
        val text: TextView = itemView.findViewById(R.id.note_card_text)
        val date: TextView = itemView.findViewById(R.id.note_card_date)
        val cardItem: CardView = itemView.findViewById(R.id.noteItemCard)
        val favouriteMark :ImageView = itemView.findViewById(R.id.isFavouriteMark)

        init {
            cardItem.setOnCreateContextMenuListener(this)

        }

        override fun onCreateContextMenu(menu: ContextMenu?, p1: View?, p2: ContextMenu.ContextMenuInfo?)
        {
            (context as Activity).menuInflater.inflate(R.menu.note_card_context_menu, menu)
            MenuCompat.setGroupDividerEnabled(menu, true)
        }

    }

    public fun setData(notes : List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

}