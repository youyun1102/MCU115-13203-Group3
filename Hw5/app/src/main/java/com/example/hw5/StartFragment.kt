package com.example.hw5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class StartFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnStart = view.findViewById<Button>(R.id.btnStart)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnStart.setOnClickListener {
            viewModel.startWork()
            val viewPager = requireActivity().findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewPager)
            viewPager.currentItem = 1
        }

        btnCancel.setOnClickListener {
            viewModel.cancelWork()
        }
    }
}
