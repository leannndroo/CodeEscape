package com.example.codeescape.utils

import android.content.Context
import android.media.MediaPlayer
import com.example.codeescape.R

object MusicManager {

    private var mediaPlayer: MediaPlayer? = null
    private var isPaused = false

    fun iniciar(context: Context) {
        // Si ya está sonando no hacer nada
        if (mediaPlayer?.isPlaying == true) return

        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.musica_menu)
                mediaPlayer?.isLooping = true
                mediaPlayer?.setVolume(0.8f, 0.8f)
            }
            mediaPlayer?.start()
            isPaused = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pausar() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                isPaused = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun reanudar() {
        try {
            if (isPaused) {
                mediaPlayer?.start()
                isPaused = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun detener() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            isPaused = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setVolumen(volumen: Float) {
        mediaPlayer?.setVolume(volumen, volumen)
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}