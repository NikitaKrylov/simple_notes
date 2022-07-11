package com.example.noteapplication.dock_instrument

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.noteapplication.R

class HtmlTagEditorFragment : Fragment(), View.OnClickListener {
    private lateinit var backToToolsButton : ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backToToolsButton = view.findViewById(R.id.back_to_tools_btn)
        backToToolsButton.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_html_tag_editor, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HtmlTagEditorFragment()
    }

    private fun backToTools() {
        findNavController().navigate(R.id.action_htmlTagEditorFragment_to_defaultToolsFragment)
    }

    override fun onClick(view: View?) {
        when (view?.id){
            backToToolsButton.id -> backToTools()
        }
    }
}