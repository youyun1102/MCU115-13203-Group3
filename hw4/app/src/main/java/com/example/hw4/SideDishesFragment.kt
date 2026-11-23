package com.example.hw4

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2

class SideDishesFragment : Fragment(R.layout.fragment_side_dishes) {
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cbFries = view.findViewById<CheckBox>(R.id.cbFries)
        val cbSalad = view.findViewById<CheckBox>(R.id.cbSalad)
        val cbCorn = view.findViewById<CheckBox>(R.id.cbCorn)
        val btnNext = view.findViewById<Button>(R.id.btnNext2)

        cbFries.setOnCheckedChangeListener { _, isChecked -> viewModel.toggleSideDish("Fries", isChecked) }
        cbSalad.setOnCheckedChangeListener { _, isChecked -> viewModel.toggleSideDish("Salad", isChecked) }
        cbCorn.setOnCheckedChangeListener { _, isChecked -> viewModel.toggleSideDish("Corn Cup", isChecked) }

        btnNext.setOnClickListener {
            activity?.findViewById<ViewPager2>(R.id.viewPager)?.currentItem = 2
        }
    }
}