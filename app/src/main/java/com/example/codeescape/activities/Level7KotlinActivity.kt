package com.example.codeescape.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.dialogs.VolumeDialog
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.GameManager
import com.example.codeescape.utils.MusicManager

class Level7KotlinActivity : AppCompatActivity() {

    private var sublevel1Done = false
    private var sublevel2Done = false
    private var sublevel3Done = false
    private var playerPuerta: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_level7_kotlin)

        playerPuerta = MediaPlayer.create(this, R.raw.sonido_puerta)
        GameManager.reiniciarVidas()

        val btnObj1: Button = findViewById(R.id.btnKtObj1)
        val btnObj2: Button = findViewById(R.id.btnKtObj2)
        val btnDoor: Button = findViewById(R.id.btnKtDoor7)
        val btnBack: Button = findViewById(R.id.btnBackKt7)
        val btnVolume: Button = findViewById(R.id.btnVolume)

        btnVolume.setOnClickListener { VolumeDialog.show(this) }

        btnObj1.setOnClickListener {
            val intent = Intent(this, SubLevelKotlinActivity::class.java)
            intent.putExtra("SUBLEVEL", 1)
            intent.putExtra("NIVEL", 7)
            startActivityForResult(intent, 1)
            AnimManager.avanzar(this)
        }

        btnObj2.setOnClickListener {
            if (!sublevel1Done) {
                Toast.makeText(this, "⚠️ Primero el primer objeto", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SubLevelKotlinActivity::class.java)
                intent.putExtra("SUBLEVEL", 2)
                intent.putExtra("NIVEL", 7)
                startActivityForResult(intent, 2)
                AnimManager.avanzar(this)
            }
        }

        btnDoor.setOnClickListener {
            when {
                !sublevel1Done || !sublevel2Done -> {
                    Toast.makeText(this, "🔒 Resuelve los acertijos primero", Toast.LENGTH_SHORT).show()
                }
                sublevel3Done -> mostrarVictoria()
                else -> {
                    val intent = Intent(this, SubLevelKotlinActivity::class.java)
                    intent.putExtra("SUBLEVEL", 3)
                    intent.putExtra("NIVEL", 7)
                    startActivityForResult(intent, 3)
                    AnimManager.avanzar(this)
                }
            }
        }

        btnBack.setOnClickListener {
            finish()
            AnimManager.volver(this)
        }
    }

    override fun onResume() {
        super.onResume()
        MusicManager.iniciar(this)
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pausar()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1 -> {
                    sublevel1Done = true
                    actualizarProgreso()
                    Toast.makeText(this, "✅ Suspend dominado", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    sublevel2Done = true
                    actualizarProgreso()
                    Toast.makeText(this, "✅ Flow controlado", Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    sublevel3Done = true
                    actualizarProgreso()
                    mostrarVictoria()
                }
            }
        }
    }

    private fun actualizarProgreso() {
        val tvProgress: TextView = findViewById(R.id.tvKtProgress7)
        val s1 = if (sublevel1Done) "✅" else "[ ]"
        val s2 = if (sublevel2Done) "✅" else "[ ]"
        val s3 = if (sublevel3Done) "✅" else "[ ]"
        tvProgress.text = "PROGRESO: $s1 $s2 $s3"

        if (sublevel1Done && sublevel2Done) {
            val btnDoor: Button = findViewById(R.id.btnKtDoor7)
            val tvDoorStatus: TextView = findViewById(R.id.tvKtDoorStatus7)
            btnDoor.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#3A1A00")
            )
            tvDoorStatus.text = "INTENTA ABRIRLA"
            tvDoorStatus.setTextColor(android.graphics.Color.parseColor("#FFA500"))
        }
    }

    private fun mostrarVictoria() {
        playerPuerta?.start()
        val tvDoorStatus: TextView = findViewById(R.id.tvKtDoorStatus7)
        val btnDoor: Button = findViewById(R.id.btnKtDoor7)

        btnDoor.backgroundTintList = android.content.res.ColorStateList.valueOf(
            android.graphics.Color.parseColor("#00FF41")
        )
        btnDoor.setTextColor(android.graphics.Color.parseColor("#0A0A0A"))
        tvDoorStatus.text = "ABIERTA"
        tvDoorStatus.setTextColor(android.graphics.Color.parseColor("#00FF41"))

        val prefs = getSharedPreferences("CodeEscape", MODE_PRIVATE)
        prefs.edit().putBoolean("kt_nivel7_completo", true).apply()
        GameManager.nivelCompletado()
        GameManager.guardar(this)

        AlertDialog.Builder(this)
            .setTitle("🎉 ¡LEGADO COMPLETADO!")
            .setMessage("Has dominado la herencia en Kotlin.\n\n> Nivel 6 desbloqueado.")
            .setPositiveButton("CONTINUAR") { dialog, _ ->
                dialog.dismiss()
                finish()
                AnimManager.volver(this)
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerPuerta?.release()
    }
}