package com.example.myapphw7

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    private var isReceiverRegistered = false

    private val timerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == TimerService.ACTION_TIMER_UPDATE) {
                val formattedTime = intent.getStringExtra(TimerService.EXTRA_FORMATTED_TIME) ?: "00:00"
                val isFinished = intent.getBooleanExtra(TimerService.EXTRA_IS_FINISHED, false)

                tvTimer.text = formattedTime
                if (isFinished) {
                    updateUiToReadyState()
                } else {
                    updateUiToFocusingState()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvStatus = findViewById(R.id.tvStatus)
        tvTimer = findViewById(R.id.tvTimer)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)

        updateUiToReadyState()

        btnStart.setOnClickListener {
            registerTimerReceiver()
            val startIntent = Intent(this, TimerService::class.java).apply {
                action = TimerService.ACTION_START
            }
            startService(startIntent)
            updateUiToFocusingState()
        }

        btnStop.setOnClickListener {
            val stopIntent = Intent(this, TimerService::class.java).apply {
                action = TimerService.ACTION_STOP
            }
            startService(stopIntent)
            updateUiToReadyState()
        }
    }

    private fun registerTimerReceiver() {
        if (!isReceiverRegistered) {
            val filter = IntentFilter(TimerService.ACTION_TIMER_UPDATE)
            ContextCompat.registerReceiver(
                this,
                timerReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            isReceiverRegistered = true
        }
    }

    private fun unregisterTimerReceiver() {
        if (isReceiverRegistered) {
            try {
                unregisterReceiver(timerReceiver)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isReceiverRegistered = false
        }
    }

    private fun updateUiToReadyState() {
        tvStatus.text = getString(R.string.status_ready)
        tvStatus.setTextColor(ContextCompat.getColor(this, R.color.brand_green))
        tvTimer.text = getString(R.string.default_timer)
    }

    private fun updateUiToFocusingState() {
        tvStatus.text = getString(R.string.status_focusing)
        tvStatus.setTextColor(ContextCompat.getColor(this, R.color.brand_red))
    }

    override fun onStart() {
        super.onStart()
        registerTimerReceiver()
    }

    override fun onDestroy() {
        unregisterTimerReceiver()
        super.onDestroy()
    }
}