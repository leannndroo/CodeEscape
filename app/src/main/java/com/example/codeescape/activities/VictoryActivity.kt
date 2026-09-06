package com.example.codeescape.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.GameManager
import com.example.codeescape.utils.MusicManager

class VictoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_victory)

        val btnPlayAgain: Button = findViewById(R.id.btnPlayAgain)
        val btnExit: Button = findViewById(R.id.btnExit)

        pedirNombreYGuardar()

        btnPlayAgain.setOnClickListener {
            val prefs = getSharedPreferences("CodeEscape", MODE_PRIVATE)
            prefs.edit().clear().apply()
            GameManager.resetear()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            AnimManager.fade(this)
        }

        btnExit.setOnClickListener {
            MusicManager.detener()
            finishAffinity()
        }
    }

    private fun pedirNombreYGuardar() {
        val input = EditText(this)
        input.hint = "Escribe tu nombre"
        input.setTextColor(android.graphics.Color.parseColor("#00FF41"))
        input.setHintTextColor(android.graphics.Color.parseColor("#555555"))
        input.setPadding(32, 16, 32, 16)

        AlertDialog.Builder(this, R.style.DialogTheme)
            .setTitle("🏆 ¡FELICITACIONES!")
            .setMessage("Ingresa tu nombre para\nguardar tu puntaje")
            .setView(input)
            .setPositiveButton("GUARDAR") { dialog, _ ->
                val nombre = input.text.toString().trim().ifEmpty { "Jugador" }
                LeaderboardActivity.guardarPuntaje(this, nombre, GameManager.puntosTotal)
                GameManager.guardar(this)
                dialog.dismiss()
            }
            .setCancelable(false)
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
}