package com.example.hw4

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ConfirmFragment : Fragment(R.layout.fragment_confirm) {
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvSummary = view.findViewById<TextView>(R.id.tvSummaryPreview)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)

        viewModel.order.observe(viewLifecycleOwner) { order ->
            tvSummary.text = "Main: ${order.mainMeal ?: "Not selected"}\n" +
                    "Sides: ${if (order.sideDishes.isEmpty()) "Not selected" else order.sideDishes.joinToString(", ")}\n" +
                    "Drink: ${order.drink ?: "Not selected"}"
        }

        btnConfirm.setOnClickListener {
            val order = viewModel.order.value

            // 檢查是否漏點
            if (order?.isComplete() != true) {
                Toast.makeText(
                    requireContext(),
                    "Please select a main meal, at least one side dish, and a drink.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // 彈出確認對話框
            AlertDialog.Builder(requireContext())
                .setTitle("Submit Order")
                .setMessage(
                    "Main: ${order.mainMeal}\n" +
                            "Sides: ${order.sideDishes.joinToString(", ")}\n" +
                            "Drink: ${order.drink}\n\nSubmit this order?"
                )
                .setPositiveButton("Submit") { dialog, _ ->
                    val summaryText = "Main: ${order.mainMeal} | Sides: ${order.sideDishes.joinToString(", ")} | Drink: ${order.drink}"
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Order submitted!", Toast.LENGTH_SHORT).show()

                    // 回傳 MainActivity 顯示並重設
                    (activity as? MainActivity)?.handleOrderSubmitted(summaryText)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}