package com.example.codeescape

import android.app.Application
import com.example.codeescape.utils.MusicManager

class CodeEscapeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Iniciar música desde que arranca la app
        MusicManager.iniciar(this)
    }
}