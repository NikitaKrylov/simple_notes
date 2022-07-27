package com.example.noteapplication.adapter

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val noteView = LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false)
        return NoteAdapter.NoteViewHolder(noteView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note =  notes[position]

        holder.text.maxLines = preferences.getInt("max_note_card_line_preferences", R.integer.default_max_note_card_line_count)

        holder.title.text = NoteActivity.fromHtml(note.title)
        holder.text.text = NoteActivity.fromHtml(note.text)
        holder.date.text = SimpleDateFormat("MMM d, HH:mm").format(note.creationDate)
        holder.favouriteMark.visibility = when (note.isFavourite) {1 -> ImageView.VISIBLE else -> ImageView.INVISIBLE}


        val color = context.getColor(note.backgroundColorId)
        holder.cardItem.setCardBackgroundColor(color)

        holder.cardItem.setOnClickListener {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra("IS_SAVE_MOD", true)
            intent.putExtra("NOTE_ID", note.id)

            startActivity(context, intent, null )

        }



    }

    fun getNoteId(groupId: Int): Int{
        return notes[groupId].id
    }
    fun getNote(groupId: Int): Note{
        return notes[groupId]
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{
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
            menu?.add(this.adapterPosition, R.id.share_note_btn, 0, "Share")
            menu?.add(this.adapterPosition, R.id.delete_menu_btn, 1, "Delete")
        }


    }

    public fun setData(notes : List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

}