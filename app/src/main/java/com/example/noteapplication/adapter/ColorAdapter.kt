package com.example.noteapplication.adapter

import ColorPickerFragment
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
import com.example.noteapplication.model.Color
import java.lang.Exception

class ColorAdapter(private var context: Context, private var colors:List<Color>, private val colorPickerImplementation:ColorPickerFragment) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val noteItemView = LayoutInflater.from(context).inflate(R.layout.color_item, parent, false)
        return ColorViewHolder(noteItemView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        try {
            var color = context.getColor(colors[position].colorId)
            holder.cardView.setCardBackgroundColor(color)
        }
        catch (e:Exception) {}

//        holder.colorName.text = colors[position].text
//        holder.cardView.setOnClickListener(onClickListener)
        holder.cardView.setOnClickListener {
            colorPickerImplementation.setBackgroundColor(colors[position].colorId)
        }
    }

    override fun getItemCount(): Int = colors.size

    class ColorViewHolder(var item: View) : RecyclerView.ViewHolder(item){
//        var colorName: TextView = itemView.findViewById(R.id.card_view_text)
        var cardView: CardView = itemView.findViewById(R.id.card_view_item)
    }

}