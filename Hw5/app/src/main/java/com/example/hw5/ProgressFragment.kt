package com.example.hw5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.progressindicator.CircularProgressIndicator

class ProgressFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val circularProgressBar = view.findViewById<CircularProgressIndicator>(R.id.circularProgressBar)
        val tvPercentage = view.findViewById<TextView>(R.id.tvPercentage)
        val tvProgressStatus = view.findViewById<TextView>(R.id.tvProgressStatus)

        viewModel.workState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SharedViewModel.WorkState.IDLE -> {
                    circularProgressBar.progress = 0
                    tvPercentage.text = "0%"
                    tvProgressStatus.text = "Idle"
                }
                SharedViewModel.WorkState.PREPARING -> {
                    circularProgressBar.progress = 0
                    tvPercentage.text = "0%"
                    tvProgressStatus.text = "準備中...."
                }
                SharedViewModel.WorkState.WORKING -> {
                    // handled by progress observer
                }
                SharedViewModel.WorkState.FINISHED -> {
                    circularProgressBar.progress = 100
                    tvPercentage.text = "100%"
                    tvProgressStatus.text = "背景工作結束！"
                }
                SharedViewModel.WorkState.CANCELED -> {
                    tvProgressStatus.text = "Canceled"
                }
            }
        }

        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            if (viewModel.workState.value == SharedViewModel.WorkState.WORKING) {
                circularProgressBar.progress = progress
                tvPercentage.text = "$progress%"
                tvProgressStatus.text = "Working... $progress%"
            }
        }
    }
}
