package com.example.myapphw5th

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.progressindicator.CircularProgressIndicator

class ProgressFragment : Fragment(R.layout.frag_progress) {
    private val vm: WorkViewModel by lazy {
        ViewModelProvider(requireActivity())[WorkViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val circularProgress = view.findViewById<CircularProgressIndicator>(R.id.circularProgress)
        val txtCenter = view.findViewById<TextView>(R.id.txtCenter)
        val txtStatus = view.findViewById<TextView>(R.id.txtStatus)

        vm.status.observe(viewLifecycleOwner) { status ->
            when {
                status == "Idle" -> {
                    circularProgress.isIndeterminate = false
                    circularProgress.progress = 0
                    txtCenter.text = "0%"
                    txtStatus.text = "Idle"
                }
                status.startsWith("Preparing") -> {
                    circularProgress.isIndeterminate = true
                    txtCenter.text = "Preparing..."
                    txtStatus.text = "Preparing..."
                }
                status.startsWith("Working") -> {
                    circularProgress.isIndeterminate = false
                }
                status == "背景工作結束!" || status == "背景工作結束" -> {
                    circularProgress.isIndeterminate = false
                    circularProgress.progress = 100
                    txtCenter.text = "100%"
                    txtStatus.text = "背景工作結束!"
                }
                status == "Canceled" -> {
                    circularProgress.isIndeterminate = false
                    txtCenter.text = "Canceled"
                    txtStatus.text = "Canceled"
                }
                else -> {
                    txtStatus.text = status
                }
            }
        }

        vm.progress.observe(viewLifecycleOwner) { p ->
            val currentStatus = vm.status.value ?: ""
            if (currentStatus.startsWith("Working")) {
                circularProgress.isIndeterminate = false
                circularProgress.max = 100
                circularProgress.setProgress(p, true)
                txtCenter.text = "$p%"
                txtStatus.text = "Working... $p%"
            }
        }
    }
}