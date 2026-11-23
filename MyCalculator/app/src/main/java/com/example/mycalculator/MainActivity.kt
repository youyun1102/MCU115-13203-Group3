package com.example.mycalculator // 請確認這裡是你專案原本的 package 名稱

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var tvInput: TextView
    private lateinit var tvOldInput: TextView

    private var currentInput = ""
    private var oldInput = ""
    private var selectedOperator = ""
    private var isNewInput = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)
        tvOldInput = findViewById(R.id.tvOldInput)

        // 數字按鈕監聽
        val numberButtons = mapOf(
            R.id.btnZero to "0",
            R.id.btnOne to "1",
            R.id.btnTwo to "2",
            R.id.btnThree to "3",
            R.id.btnFour to "4",
            R.id.btnFive to "5",
            R.id.btnSix to "6",
            R.id.btnSeven to "7",
            R.id.btnEight to "8",
            R.id.btnNine to "9",
            R.id.btnDot to "."
        )

        for ((id, digit) in numberButtons) {
            findViewById<Button>(id).setOnClickListener {
                appendNumber(digit)
            }
        }

        // 運算子按鈕監聽 (+, -, x, /)
        val operatorButtons = mapOf(
            R.id.btnPlus to "+",
            R.id.btnMinus to "-",
            R.id.btnMultiply to "x",
            R.id.btnDivide to "/"
        )

        for ((id, op) in operatorButtons) {
            findViewById<Button>(id).setOnClickListener {
                selectOperator(op)
            }
        }

        // 等于按鈕
        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            calculateResult()
        }

        // AC 全清
        findViewById<Button>(R.id.allClear).setOnClickListener {
            clearAll()
        }

        // C 清除當前輸入
        findViewById<Button>(R.id.clear).setOnClickListener {
            currentInput = "0"
            tvInput.text = currentInput
            isNewInput = true
        }

        // 退格 (⌫)
        findViewById<Button>(R.id.btnBackSpace).setOnClickListener {
            if (currentInput.isNotEmpty() && currentInput != "0") {
                currentInput = currentInput.dropLast(1)
                if (currentInput.isEmpty() || currentInput == "-") {
                    currentInput = "0"
                    isNewInput = true
                }
                tvInput.text = currentInput
            }
        }
    }

    private fun appendNumber(digit: String) {
        if (isNewInput) {
            currentInput = if (digit == ".") "0." else digit
            isNewInput = false
        } else {
            if (digit == "." && currentInput.contains(".")) return
            if (currentInput == "0" && digit != ".") {
                currentInput = digit
            } else {
                currentInput += digit
            }
        }
        tvInput.text = currentInput
    }

    private fun selectOperator(op: String) {
        if (oldInput.isNotEmpty() && !isNewInput) {
            calculateResult()
        }
        oldInput = currentInput
        selectedOperator = op
        tvOldInput.text = "$oldInput $selectedOperator"
        isNewInput = true
    }

    private fun calculateResult() {
        if (oldInput.isEmpty() || selectedOperator.isEmpty()) return

        val num1 = oldInput.toDoubleOrNull() ?: 0.0
        val num2 = currentInput.toDoubleOrNull() ?: 0.0
        var result = 0.0

        when (selectedOperator) {
            "+" -> result = num1 + num2
            "-" -> result = num1 - num2
            "x" -> result = num1 * num2
            "/" -> {
                if (num2 == 0.0) {
                    tvInput.text = "錯誤"
                    clearAll()
                    return
                }
                result = num1 / num2
            }
        }
        if (result == result.toLong().toDouble()) {
            currentInput = result.toLong().toString()
        } else {
            currentInput = result.toString()
        }

        tvInput.text = currentInput
        tvOldInput.text = ""
        oldInput = ""
        selectedOperator = ""
        isNewInput = true
    }

    private fun clearAll() {
        currentInput = "0"
        oldInput = ""
        selectedOperator = ""
        tvInput.text = "0"
        tvOldInput.text = ""
        isNewInput = true
    }
}