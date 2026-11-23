

package com.example.hw4

data class Order(
    var mainMeal: String? = null,
    val sideDishes: MutableList<String> = mutableListOf(),
    var drink: String? = null
) {
    fun isComplete(): Boolean {
        return !mainMeal.isNullOrEmpty() && sideDishes.isNotEmpty() && !drink.isNullOrEmpty()
    }
}
