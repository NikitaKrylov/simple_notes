package com.example.noteapplication.adapter

import com.example.noteapplication.dock_instrument.ColorPickerFragment
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.R
import com.example.noteapplication.model.Color
import java.lang.Exception

class ColorAdapter(private var context: Context, private var colors:List<Color>) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val noteItemView = LayoutInflater.from(context).inflate(R.layout.color_item, parent, false)
        return ColorViewHolder(noteItemView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        try {
            val color = context.getColor(colors[position].colorId)
            holder.cardView.setCardBackgroundColor(color)
        }
        catch (e:Exception) {}

        holder.cardView.setOnClickListener {
            ColorPickerFragment.setBackgroundColor(context as Activity, colors[position].colorId)
        }
    }

    override fun getItemCount(): Int = colors.size

    class ColorViewHolder(var item: View) : RecyclerView.ViewHolder(item){
        var cardView: CardView = itemView.findViewById(R.id.card_view_item)
    }

}