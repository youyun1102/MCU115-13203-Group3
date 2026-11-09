package com.example.hw5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ResultFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvResultStatus = view.findViewById<TextView>(R.id.tvResultStatus)

        viewModel.workState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SharedViewModel.WorkState.IDLE -> {
                    tvResultStatus.text = "Idle"
                }
                SharedViewModel.WorkState.PREPARING -> {
                    tvResultStatus.text = "準備中..."
                }
                SharedViewModel.WorkState.WORKING -> {
                    tvResultStatus.text = "工作中..."
                }
                SharedViewModel.WorkState.FINISHED -> {
                    tvResultStatus.text = "背景工作結束！"
                }
                SharedViewModel.WorkState.CANCELED -> {
                    tvResultStatus.text = "Idle"
                }
            }
        }
    }
}
