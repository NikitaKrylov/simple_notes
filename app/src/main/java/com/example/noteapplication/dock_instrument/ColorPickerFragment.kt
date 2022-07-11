package com.example.noteapplication.dock_instrument
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.NoteActivity
import com.example.noteapplication.R
import com.example.noteapplication.adapter.ColorAdapter
import com.example.noteapplication.model.Color

class ColorPickerFragment() : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_color_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setColorRecycler(view)
        view.findViewById<ImageButton>(R.id.back_to_tools_btn).setOnClickListener(this)
    }

    private fun setColorRecycler(view:View){
        colors.clear()
        colors.add(Color(R.color.default_note_background, "Default"))
        colors.add(Color(R.color.Green, "Green"))
        colors.add(Color(R.color.PeriwinkleCrayola, "PeriwinkleCrayola"))
        colors.add(Color(R.color.BrightNavyBlue, "BrightNavyBlue"))
        colors.add(Color(R.color.RadicalRed, "RadicalRed"))
        colors.add(Color(R.color.AntiqueBrass, "AntiqueBrass"))
        colors.add(Color(R.color.MiddleYellow, "MiddleYellow"))


        val adapter = ColorAdapter(context as Context, colors)
        val recyclerColor = view.findViewById<RecyclerView>(R.id.recyclerBackgroundColor)

        recyclerColor.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerColor.adapter = adapter
    }



    companion object {
        var backgroundColorId:Int ?= null
        val colors = mutableListOf<Color>()

        @JvmStatic
        fun newInstance()  = ColorPickerFragment()

        fun setBackgroundColor(activity:Activity,  colorId : Int){
            backgroundColorId = colorId
            activity.findViewById<CoordinatorLayout>(R.id.note_background)?.setBackgroundColor(getColor(activity, colorId))
            NoteActivity.setNavigationBarColor(activity, getColor(activity, colorId))
            activity.window.statusBarColor = getColor( activity,colorId)
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(activity , colorId)))

        }
    }

    override fun onClick(view: View?) {
        when (view?.id){
            R.id.back_to_tools_btn -> backToTools()
        }
    }

    private fun backToTools() {
        findNavController().navigate(R.id.action_colorPickerFragment_to_defaultToolsFragment)
    }




}