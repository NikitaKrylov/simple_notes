@file:Suppress("RedundantSemicolon")

package com.example.noteapplication.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.NoteActivity
import com.example.noteapplication.R
import com.example.noteapplication.TrashNoteActivity
import com.example.noteapplication.model.Note
import java.text.SimpleDateFormat

class TrashNoteAdapter(val context: Context): RecyclerView.Adapter<TrashNoteAdapter.NoteViewHolder>() {
    private var notes = emptyList<Note>()
    private var preferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var position: Int = 0;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val noteView = LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false)
        return NoteViewHolder(noteView, context)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note =  notes[position]
        val color = context.getColor(note.backgroundColorId)

        if (note.text.isEmpty()){
            holder.text.visibility = View.GONE
        }
        else{
            holder.text.visibility = View.VISIBLE
            holder.text.maxLines = preferences.getInt("max_note_card_line_preferences", R.integer.default_max_note_card_line_count)
            holder.text.text = note.text
        }

        holder.apply {
            if (note.text.isEmpty()){
                text.visibility = View.GONE
            }
            else{
                text.visibility = View.VISIBLE
                text.maxLines = preferences.getInt("max_note_card_line_preferences", R.integer.default_max_note_card_line_count)
            }

            title.text = note.title
            date.text = SimpleDateFormat("d MMM HH:mm").format(note.creationDate)
            favouriteMark.visibility = when (note.isFavourite) {true -> ImageView.VISIBLE else -> ImageView.GONE}

            cardItem.apply {
                setCardBackgroundColor(color)
                setOnClickListener {
                    val intent = TrashNoteActivity.getIntent(context, note.id)
                    ContextCompat.startActivity(context, intent, null)
                }
                setOnLongClickListener {
                    setPosition(position)
                    it.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                    false
                }
            }

        }

    }

    fun getNote(position: Int = this.position): Note = notes[position]

    override fun getItemCount(): Int = notes.size

    fun getPosition(): Int = position

    private fun setPosition(position: Int){
        this.position = position
    }
    fun setData(notes : List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    class NoteViewHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        val title: TextView = itemView.findViewById(R.id.note_card_title)
        val text: TextView = itemView.findViewById(R.id.note_card_text)
        val date: TextView = itemView.findViewById(R.id.note_card_date)
        val cardItem: CardView = itemView.findViewById(R.id.noteItemCard)
        val favouriteMark :ImageView = itemView.findViewById(R.id.isFavouriteMark)

        init {
            cardItem.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, p1: View?, p2: ContextMenu.ContextMenuInfo?) {
            (context as Activity).menuInflater.inflate(R.menu.trash_note_card_context_menu, menu)
            MenuCompat.setGroupDividerEnabled(menu, true)
        }

    }
}