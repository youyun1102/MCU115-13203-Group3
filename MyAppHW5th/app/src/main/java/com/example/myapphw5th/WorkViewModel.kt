package com.example.myapphw5th

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WorkViewModel(application: Application) : AndroidViewModel(application) {
    private val handlerThread = HandlerThread("VM-Work").apply { start() }
    private val worker = Handler(handlerThread.looper)

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _status = MutableLiveData("Idle")
    val status: LiveData<String> = _status

    @Volatile
    private var running = false

    @Volatile
    private var workerThread: Thread? = null

    // ===== 作業六：音樂播放器控制變數 =====
    private var mediaPlayer: MediaPlayer? = null

    // 開始播放背景音樂 (播放 res/raw/background_music.mp3)
    private fun startMusic() {
        stopMusic()
        try {
            val app = getApplication<Application>()
            mediaPlayer = MediaPlayer.create(app, R.raw.background_music)?.apply {
                setVolume(1.0f, 1.0f) // 設定播放器音量為最大 (100%)
                isLooping = true
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 結束/停止播放背景音樂並釋放資源
    private fun stopMusic() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
        }
    }
    // ===================================

    fun start() {
        if (running) return
        running = true
        _status.postValue("Preparing...")
        _progress.postValue(0)

        // ===== 作業六：開始背景工作時播放背景音樂 =====
        startMusic()

        worker.post {
            workerThread = Thread.currentThread()
            try {
                // Preparation phase (~3 seconds)
                for (k in 1..30) {
                    if (!running) break
                    Thread.sleep(100)
                }

                if (running) {
                    _status.postValue("Working...")
                    for (i in 1..100) {
                        if (!running) break
                        Thread.sleep(100)
                        _progress.postValue(i)
                    }
                }

                if (running) {
                    _status.postValue("背景工作結束!")
                    _progress.postValue(100)
                } else {
                    _status.postValue("Canceled")
                }
            } catch (_: InterruptedException) {
                _status.postValue("Canceled")
            } finally {
                running = false
                workerThread = null
                // ===== 作業六：背景工作完成或中斷時結束音樂 =====
                stopMusic()
            }
        }
    }

    fun cancel() {
        if (running) {
            running = false
            _status.postValue("Canceled")
            workerThread?.interrupt()
            // ===== 作業六：點擊 Cancel 時結束音樂 =====
            stopMusic()
        }
    }

    override fun onCleared() {
        running = false
        // ===== 作業六：ViewModel 銷毀時停止音樂 =====
        stopMusic()
        workerThread?.interrupt()
        handlerThread.quitSafely()
        super.onCleared()
    }
}
