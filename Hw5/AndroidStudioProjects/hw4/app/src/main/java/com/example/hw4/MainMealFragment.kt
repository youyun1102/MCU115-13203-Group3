package com.example.hw4

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2

class MainMealFragment : Fragment(R.layout.fragment_main_meal) {
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rgMainMeal = view.findViewById<RadioGroup>(R.id.rgMainMeal)
        val btnNext = view.findViewById<Button>(R.id.btnNext1) // 假設佈局中有這個按鈕 ID

        btnNext.setOnClickListener {
            when (rgMainMeal.checkedRadioButtonId) {
                R.id.rbBurger -> viewModel.setMainMeal("Burger")
                R.id.rbChicken -> viewModel.setMainMeal("Fried Chicken")
                R.id.rbNuggets -> viewModel.setMainMeal("Chicken Nuggets")
            }
            activity?.findViewById<ViewPager2>(R.id.viewPager)?.currentItem = 1
        }
    }
}