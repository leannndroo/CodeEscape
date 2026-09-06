package com.example.codeescape.utils

import android.app.Activity
import com.example.codeescape.R

/**
 * AnimManager — Maneja las animaciones de transición
 * entre pantallas de forma consistente
 */
object AnimManager {

    fun avanzar(activity: Activity) {
        activity.overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    fun volver(activity: Activity) {
        activity.overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

    fun fade(activity: Activity) {
        activity.overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }
}