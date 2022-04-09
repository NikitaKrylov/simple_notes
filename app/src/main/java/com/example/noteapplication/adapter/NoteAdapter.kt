package com.example.noteapplication.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.MainActivity
import com.example.noteapplication.NoteActivity
import com.example.noteapplication.R
import com.example.noteapplication.model.Note

class NoteAdapter(private var context: Context) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes = emptyList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val noteView = LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false)
        return NoteAdapter.NoteViewHolder(noteView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note =  notes[position]

        holder.title.text = note.title
        holder.text.text = note.text
        holder.date.text = note.creationDatetime

        val color = context.getColor(note.backgroundColorId)
        holder.cardItem.setCardBackgroundColor(color)

        holder.cardItem.setOnClickListener {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra("IS_SAVE_MOD", true)
            intent.putExtra("NOTE_ID", note.id)

            startActivity(context, intent, null )

        }

    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.note_card_title)
        val text: TextView = itemView.findViewById(R.id.note_card_text)
        val date: TextView = itemView.findViewById(R.id.note_card_date)
        val cardItem: CardView = itemView.findViewById(R.id.noteItemCard)
    }

    public fun setData(notes : List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }
}