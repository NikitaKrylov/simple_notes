
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.R
import com.example.noteapplication.adapter.FontAdapter
import com.example.noteapplication.model.Font

class FontPickerFragment : Fragment(), View.OnClickListener {

    private lateinit var backToToolsButton : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_font_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setColorRecycler(view)
        backToToolsButton = view.findViewById(R.id.back_to_tools_btn)
        backToToolsButton.setOnClickListener(this)
    }


    private fun setColorRecycler(view:View){
        val fonts = mutableListOf<Font>()
//        fonts.add(Font(android.R.attr.fontFamily, "DefaultFont"))
        fonts.add(Font(R.font.apercu_medium, "ApercuMedium"))

        val adapter = FontAdapter(context as Context, fonts, this)
        val recyclerFont = view.findViewById<RecyclerView>(R.id.recyclerFont)

        recyclerFont.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerFont.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance() = FontPickerFragment()

    }

    override fun onClick(view: View?) {
        when (view?.id){
            backToToolsButton.id -> backToTools()
            R.id.card_view_item -> {setFont(view as CardView)}
        }
    }

    private fun setFont(view: CardView) {
        val typeface = view.findViewById<TextView>(R.id.font_card_text).typeface
//        parentFragment?.parentFragment?.view?.findViewById<EditText>(R.id.noteTitle)?.typeface = typeface
//        parentFragment?.parentFragment?.view?.findViewById<EditText>(R.id.noteText)?.typeface = typeface

    }

    private fun backToTools() {
        findNavController().navigate(R.id.action_fontPickerFragment_to_defaultToolsFragment)
    }
}