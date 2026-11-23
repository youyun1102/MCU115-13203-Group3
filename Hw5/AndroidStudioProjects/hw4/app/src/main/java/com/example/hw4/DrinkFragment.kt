package com.example.hw4

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2

class DrinkFragment : Fragment(R.layout.fragment_drink) {
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rgDrink = view.findViewById<RadioGroup>(R.id.rgDrink)
        val btnNext = view.findViewById<Button>(R.id.btnNext3)

        btnNext.setOnClickListener {
            when (rgDrink.checkedRadioButtonId) {
                R.id.rbCola -> viewModel.setDrink("Cola")
                R.id.rbTea -> viewModel.setDrink("Iced Tea")
                R.id.rbWater -> viewModel.setDrink("Water")
            }
            activity?.findViewById<ViewPager2>(R.id.viewPager)?.currentItem = 3
        }
    }
}