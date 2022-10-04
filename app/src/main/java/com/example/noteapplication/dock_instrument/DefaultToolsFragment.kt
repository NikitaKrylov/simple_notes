
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.noteapplication.R

class DefaultToolsFragment : Fragment(), View.OnClickListener {
    private lateinit var colorPickerButton : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_default_tools, container, false)
        colorPickerButton = view.findViewById(R.id.show_color_picker_btn)
        colorPickerButton.setOnClickListener(this)



        return view
    }


    override fun onClick(view: View) {
        when (view.id) {
            colorPickerButton.id -> findNavController().navigate(R.id.action_defaultToolsFragment_to_colorPickerFragment)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = DefaultToolsFragment()
    }
}