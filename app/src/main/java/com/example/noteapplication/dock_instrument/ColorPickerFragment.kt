
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.MainActivity
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
        val colors = mutableListOf<Color>()



        colors.add(Color(R.color.default_note_background, "Default"))
        colors.add(Color(R.color.note_test_color, "Green"))
        colors.add(Color(R.color.TurquoiseGreen, "TurquoiseGreen"))
        colors.add(Color(R.color.OrangeCrayola, "OrangeCrayola"))
        colors.add(Color(R.color.YellowCrayola, "YellowCrayola"))
        colors.add(Color(R.color.Mauve, "Mauve"))
        colors.add(Color(R.color.LavenderWeb, "LavenderWeb"))
        colors.add(Color(R.color.Manatee, "Manatee"))
        colors.add(Color(R.color.PalePink, "PalePink"))
        colors.add(Color(R.color.Bittersweet, "Bittersweet"))


        val adapter = ColorAdapter(context as Context, colors, this)
        val recyclerColor = view.findViewById<RecyclerView>(R.id.recyclerBackgroundColor)

        recyclerColor.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerColor.adapter = adapter
    }

    fun setBackgroundColor(colorId : Int){
        ColorPickerFragment.backgroundColorId = colorId
        activity?.findViewById<ConstraintLayout>(R.id.note_background)?.setBackgroundColor(getColor(requireContext(), colorId))
        NoteActivity.setNavigationBarColor(activity as Activity, getColor(requireContext(), colorId))
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(context as Context, colorId)))

    }

    companion object {
        var backgroundColorId:Int ?= null
        @JvmStatic
        fun newInstance()  = ColorPickerFragment()
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