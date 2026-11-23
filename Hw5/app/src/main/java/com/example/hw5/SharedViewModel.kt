package com.example.hw5

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    enum class WorkState {
        IDLE,
        PREPARING,
        WORKING,
        FINISHED,
        CANCELED
    }

    private val _workState = MutableLiveData<WorkState>(WorkState.IDLE)
    val workState: LiveData<WorkState> = _workState

    private val _progress = MutableLiveData<Int>(0)
    val progress: LiveData<Int> = _progress

    private var workThread: Thread? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isCancelled = false

    fun startWork() {
        cancelWork()
        isCancelled = false
        _workState.value = WorkState.PREPARING
        _progress.value = 0

        workThread = Thread {
            try {
                // Preparing phase (e.g., 2 seconds)
                Thread.sleep(2000)
                if (isCancelled) return@Thread

                handler.post {
                    if (!isCancelled) {
                        _workState.value = WorkState.WORKING
                        _progress.value = 0
                    }
                }

                // Working phase: 0 to 100%
                for (i in 1..100) {
                    Thread.sleep(50)
                    if (isCancelled) return@Thread

                    handler.post {
                        if (!isCancelled) {
                            _progress.value = i
                        }
                    }
                }

                if (!isCancelled) {
                    handler.post {
                        _workState.value = WorkState.FINISHED
                        _progress.value = 100
                    }
                }
            } catch (e: InterruptedException) {
                // Thread interrupted
            }
        }.apply { start() }
    }

    fun cancelWork() {
        isCancelled = true
        workThread?.interrupt()
        workThread = null
        if (_workState.value == WorkState.PREPARING || _workState.value == WorkState.WORKING) {
            _workState.value = WorkState.CANCELED
        }
    }

    fun resetWork() {
        cancelWork()
        _workState.value = WorkState.IDLE
        _progress.value = 0
    }
}
