package com.example.hw4
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private val viewModel: OrderViewModel by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var tvOrderSummary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tvOrderSummary = findViewById(R.id.tvOrderSummary)

        viewPager.adapter = WizardPagerAdapter(this)
        viewPager.isUserInputEnabled = false // 鎖定左右滑動，需透過步驟點選

        viewModel.order.observe(this) { order ->
            if (order.mainMeal == null && order.sideDishes.isEmpty() && order.drink == null) {
                tvOrderSummary.text = "Your order summary will appear here."
            }
        }
    }

    // 提交成功後跳回第一頁並顯示結果
    fun handleOrderSubmitted(summary: String) {
        tvOrderSummary.text = "Last Order Result:\n$summary"
        viewModel.clearOrder()
        viewPager.currentItem = 0
    }
}