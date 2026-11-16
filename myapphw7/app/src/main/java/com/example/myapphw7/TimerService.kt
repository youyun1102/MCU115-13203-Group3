package com.example.myapphw7

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.Locale
import kotlin.concurrent.thread
import kotlin.math.ceil

class TimerService : Service() {

    companion object {
        const val ACTION_START = "com.example.myapphw7.ACTION_START"
        const val ACTION_STOP = "com.example.myapphw7.ACTION_STOP"
        const val ACTION_TIMER_UPDATE = "com.example.myapphw7.TIMER_UPDATE"
        const val EXTRA_REMAINING_SECONDS = "extra_remaining_seconds"
        const val EXTRA_FORMATTED_TIME = "extra_formatted_time"
        const val EXTRA_IS_FINISHED = "extra_is_finished"
        const val EXTRA_FOCUS_SECONDS = "extra_focus_seconds"
        const val DEFAULT_FOCUS_SECONDS = 25 * 60 // 25 分鐘 = 1500 秒
    }

    private var remainingSeconds = DEFAULT_FOCUS_SECONDS
    private var isRunning = false
    private var countdownThread: Thread? = null
    private var isSystemReceiverRegistered = false

    private val systemReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_ON -> {
                    if (isRunning) {
                        showFocusReminderToast()
                    }
                }
                Intent.ACTION_HEADSET_PLUG -> {
                    if (isRunning) {
                        val state = intent.getIntExtra("state", -1)
                        val msg = if (state == 1) "Headset plugged in" else "Headset unplugged"
                        Toast.makeText(
                            applicationContext,
                            "$msg: Ambient focus audio updated",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showFocusReminderToast() {
        val minutesLeft = ceil(remainingSeconds / 60.0).toInt()
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                applicationContext,
                "Focus Reminder: $minutesLeft mins remaining!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_HEADSET_PLUG)
        }
        try {
            ContextCompat.registerReceiver(
                this,
                systemReceiver,
                filter,
                ContextCompat.RECEIVER_EXPORTED
            )
            isSystemReceiverRegistered = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val customSeconds = intent?.getIntExtra(EXTRA_FOCUS_SECONDS, DEFAULT_FOCUS_SECONDS) ?: DEFAULT_FOCUS_SECONDS
        when (intent?.action) {
            ACTION_START -> startCountdown(customSeconds)
            ACTION_STOP -> stopCountdown()
        }
        return START_NOT_STICKY
    }

    private fun startCountdown(focusSeconds: Int = DEFAULT_FOCUS_SECONDS) {
        if (isRunning) return
        isRunning = true
        remainingSeconds = focusSeconds

        countdownThread = thread {
            var elapsedSeconds = 0
            while (isRunning && remainingSeconds > 0) {
                sendTimerBroadcast(remainingSeconds, false)

                // 每二分鐘 (120 秒) 顯示一次 Toast 提醒
                if (elapsedSeconds > 0 && elapsedSeconds % 120 == 0) {
                    showFocusReminderToast()
                }

                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    break
                }
                if (!isRunning) break
                remainingSeconds--
                elapsedSeconds++
            }

            if (isRunning && remainingSeconds <= 0) {
                isRunning = false
                sendTimerBroadcast(0, true)

                val finishIntent = Intent(this@TimerService, FinishActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(finishIntent)
                stopSelf()
            }
        }
    }

    private fun stopCountdown() {
        isRunning = false
        countdownThread?.interrupt()
        sendTimerBroadcast(0, true)
        stopSelf()
    }

    private fun sendTimerBroadcast(seconds: Int, isFinished: Boolean) {
        val minutes = seconds / 60
        val secs = seconds % 60
        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs)

        val intent = Intent(ACTION_TIMER_UPDATE).apply {
            putExtra(EXTRA_REMAINING_SECONDS, seconds)
            putExtra(EXTRA_FORMATTED_TIME, formattedTime)
            putExtra(EXTRA_IS_FINISHED, isFinished)
            setPackage(packageName)
        }
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        isRunning = false
        countdownThread?.interrupt()
        if (isSystemReceiverRegistered) {
            try {
                unregisterReceiver(systemReceiver)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isSystemReceiverRegistered = false
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}