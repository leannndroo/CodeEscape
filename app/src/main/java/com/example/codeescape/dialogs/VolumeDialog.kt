package com.example.codeescape.dialogs

import android.content.Context
import android.media.AudioManager
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import com.example.codeescape.R
import com.example.codeescape.utils.MusicManager

object VolumeDialog {

    fun show(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val dialogView = android.view.LayoutInflater.from(context)
            .inflate(R.layout.dialog_volume, null)
        val seekBar = dialogView.findViewById<SeekBar>(R.id.seekBarVolume)

        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        seekBar.max = maxVolume
        seekBar.progress = currentVolume

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                val volumen = if (maxVolume > 0) progress.toFloat() / maxVolume.toFloat() else 0f
                MusicManager.setVolumen(volumen)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        AlertDialog.Builder(context, R.style.DialogTheme)
            .setTitle("🔊 CONTROL DE VOLUMEN")
            .setView(dialogView)
            .setPositiveButton("CERRAR") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}