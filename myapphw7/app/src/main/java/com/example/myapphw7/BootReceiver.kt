package com.example.myapphw7

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Toast.makeText(
                context,
                "裝置已重啟：記得開始 Smart Pomodoro 每日學習課程！",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}