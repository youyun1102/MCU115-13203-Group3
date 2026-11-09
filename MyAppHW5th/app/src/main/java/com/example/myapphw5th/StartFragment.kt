package com.example.myapphw5th

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2

class StartFragment : Fragment(R.layout.frag_start) {
    private val vm: WorkViewModel by lazy {
        ViewModelProvider(requireActivity())[WorkViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnStart = view.findViewById<Button>(R.id.btnStart)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnStart.setOnClickListener {
            vm.start()
            // 自動切換至 Progress 頁面 (Index 1)
            requireActivity().findViewById<ViewPager2>(R.id.pager)?.currentItem = 1
        }

        btnCancel.setOnClickListener {
            vm.cancel()
        }

        vm.status.observe(viewLifecycleOwner) { status ->
            val isRunning = status.startsWith("Preparing") || status.startsWith("Working")
            btnStart.isEnabled = !isRunning
            btnCancel.isEnabled = true
        }
    }
}