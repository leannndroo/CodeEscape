package com.example.codeescape.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.dialogs.VolumeDialog
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.MusicManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

        val btnStart: Button = findViewById(R.id.btnStart)
        val btnOptions: Button = findViewById(R.id.btnOptions)
        val btnTutorial: Button = findViewById(R.id.btnTutorial)
        val tvTerminal: TextView = findViewById(R.id.tvTerminal)

        btnStart.isEnabled = false
        btnStart.alpha = 0.4f

        val mensajes = listOf(
            " Cargando juego...",
            " Inicializando niveles...",
            " Cargando recursos...",
            " Preparando acertijos...",
            " ¡Todo listo!"
        )

        var index = 0
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                tvTerminal.text = mensajes[index]
                index++
                if (index < mensajes.size) {
                    handler.postDelayed(this, 1000)
                } else {
                    btnStart.isEnabled = true
                    btnStart.alpha = 1.0f
                    tvTerminal.text = " ¡Todo listo! Pulsa EMPEZAR"
                }
            }
        }
        handler.postDelayed(runnable, 800)

        btnStart.setOnClickListener {
            startActivity(Intent(this, WorldSelectActivity::class.java))
            AnimManager.avanzar(this)
        }

        // Botón Cómo Jugar directo — sin pasar por opciones
        btnTutorial.setOnClickListener {
            startActivity(Intent(this, TutorialActivity::class.java))
            AnimManager.avanzar(this)
        }

        btnOptions.setOnClickListener {
            mostrarMenuOpciones()
        }
    }

    private fun mostrarMenuOpciones() {
        // "Cómo Jugar" eliminado — ahora está en el botón principal
        val opciones = arrayOf(
            "🔊 Volumen",
            "📊 Estadísticas",
            "👨‍💻 Créditos",
            "🏆 Mejores Puntajes"
        )

        androidx.appcompat.app.AlertDialog.Builder(this, R.style.DialogTheme)
            .setTitle("⚙️ OPCIONES")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> VolumeDialog.show(this)
                    1 -> {
                        startActivity(Intent(this, StatsActivity::class.java))
                        AnimManager.avanzar(this)
                    }
                    2 -> {
                        startActivity(Intent(this, CreditsActivity::class.java))
                        AnimManager.avanzar(this)
                    }
                    3 -> {
                        startActivity(Intent(this, LeaderboardActivity::class.java))
                        AnimManager.avanzar(this)
                    }
                }
            }
            .setNegativeButton("CERRAR") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onResume() {
        super.onResume()
        MusicManager.iniciar(this)
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pausar()
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.detener()
    }
}