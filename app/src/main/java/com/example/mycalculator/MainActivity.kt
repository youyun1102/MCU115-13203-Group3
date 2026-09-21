package com.example.mycalculator // 如果這行有紅字，點擊畫面右上角的 Change file's package 即可

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var tvInput: TextView
    private lateinit var tvOldInput: TextView
    private lateinit var tvCurrentOperand: TextView

    private var firstNumber: Double? = null
    private var currentOperator: String = ""
    private var isNewInput: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 連結 XML 元件 (完全對齊老師定義的 ID)
        tvInput = findViewById(R.id.tvInput)
        tvOldInput = findViewById(R.id.tvOldInput)
        tvCurrentOperand = findViewById(R.id.tvCurrentOperand)

        // 數字鍵與小數點
        val numberMap = mapOf(
            R.id.btnZero to "0", R.id.btnOne to "1", R.id.btnTwo to "2",
            R.id.btnThree to "3", R.id.btnFour to "4", R.id.btnFive to "5",
            R.id.btnSix to "6", R.id.btnSeven to "7", R.id.btnEight to "8",
            R.id.btnNine to "9", R.id.btnDot to "."
        )

        for ((id, value) in numberMap) {
            findViewById<Button>(id)?.setOnClickListener { onNumberClick(value) }
        }

        // 運算子鍵
        findViewById<Button>(R.id.btnPLus)?.setOnClickListener { onOperatorClick("+") }
        findViewById<Button>(R.id.btnMinus)?.setOnClickListener { onOperatorClick("-") }
        findViewById<Button>(R.id.btnMultiply)?.setOnClickListener { onOperatorClick("×") }
        findViewById<Button>(R.id.btnDivide)?.setOnClickListener { onOperatorClick("÷") }

        // 功能鍵
        findViewById<Button>(R.id.btnEqual)?.setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.allClear)?.setOnClickListener { clearAll() }
        findViewById<Button>(R.id.clear)?.setOnClickListener { clearEntry() }
        findViewById<ImageButton>(R.id.btnBackspace)?.setOnClickListener { backspace() }
    }

    private fun onNumberClick(value: String) {
        if (isNewInput) {
            tvInput.text = if (value == ".") "0." else value
            isNewInput = false
        } else {
            if (value == "." && tvInput.text.contains(".")) return
            tvInput.append(value)
        }
    }

    private fun onOperatorClick(operator: String) {
        firstNumber = tvInput.text.toString().toDoubleOrNull()
        if (firstNumber != null) {
            currentOperator = operator
            tvOldInput.text = formatValue(firstNumber!!)
            tvCurrentOperand.text = operator
            isNewInput = true
        }
    }

    private fun calculateResult() {
        if (currentOperator.isEmpty() || firstNumber == null) return

        val secondNumber = tvInput.text.toString().toDoubleOrNull() ?: return
        var result = 0.0

        when (currentOperator) {
            "+" -> result = firstNumber!! + secondNumber
            "-" -> result = firstNumber!! - secondNumber
            "×" -> result = firstNumber!! * secondNumber
            "÷" -> {
                if (secondNumber != 0.0) {
                    result = firstNumber!! / secondNumber
                } else {
                    tvInput.text = "Error"
                    resetState()
                    return
                }
            }
        }

        tvInput.text = formatValue(result)
        tvOldInput.text = ""
        tvCurrentOperand.text = ""
        resetState()
    }

    private fun clearAll() {
        tvInput.text = "0"
        tvOldInput.text = ""
        tvCurrentOperand.text = ""
        resetState()
    }

    private fun clearEntry() {
        tvInput.text = "0"
        isNewInput = true
    }

    private fun backspace() {
        if (isNewInput) return

        val currentText = tvInput.text.toString()
        if (currentText.length > 1) {
            tvInput.text = currentText.substring(0, currentText.length - 1)
        } else {
            tvInput.text = "0"
            isNewInput = true
        }
    }

    private fun resetState() {
        firstNumber = null
        currentOperator = ""
        isNewInput = true
    }

    private fun formatValue(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }
}