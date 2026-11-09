package com.example.hw4

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {
    private val _order = MutableLiveData(Order())
    val order: LiveData<Order> get() = _order

    fun setMainMeal(meal: String) {
        _order.value?.mainMeal = meal
        _order.value = _order.value
    }

    fun toggleSideDish(side: String, isChecked: Boolean) {
        val currentSides = _order.value?.sideDishes ?: mutableListOf()
        if (isChecked) {
            if (!currentSides.contains(side)) currentSides.add(side)
        } else {
            currentSides.remove(side)
        }
        _order.value?.let {
            it.sideDishes.clear()
            it.sideDishes.addAll(currentSides)
        }
        _order.value = _order.value
    }

    fun setDrink(drinkName: String) {
        _order.value?.drink = drinkName
        _order.value = _order.value
    }

    fun clearOrder() {
        _order.value = Order()
    }
}