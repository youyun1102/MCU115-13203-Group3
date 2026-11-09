package com.example.myapphw5th

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class ResultFragment : Fragment(R.layout.frag_result) {
    private val vm: WorkViewModel by lazy {
        ViewModelProvider(requireActivity())[WorkViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val status = view.findViewById<TextView>(R.id.txtResult)
        vm.status.observe(viewLifecycleOwner) { s ->
            status.text = s
        }
    }
}