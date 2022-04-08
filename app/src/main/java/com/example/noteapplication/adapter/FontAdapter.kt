package com.example.noteapplication.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.R
import com.example.noteapplication.model.Font

class FontAdapter(private var context: Context, private val fonts : List<Font>, private val onClickListener: View.OnClickListener?=null) : RecyclerView.Adapter<FontAdapter.FontViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontAdapter.FontViewHolder {
        val fontView = LayoutInflater.from(context).inflate(R.layout.font_item, parent, false)
        return FontAdapter.FontViewHolder(fontView)
    }

    override fun getItemCount(): Int {
        return fonts.size
    }

    class FontViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.font_card_text)
        val cardItem : CardView = itemView.findViewById(R.id.card_view_item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        val fontObject = fonts[position]
        holder.title.text = fontObject.name
        val typeface = context.resources.getFont(fontObject.id)
        holder.title.typeface = typeface
        holder.cardItem.setOnClickListener(onClickListener)
    }


}