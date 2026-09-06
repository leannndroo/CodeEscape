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

class Level5JavaActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_level5_java)

        playerPuerta = MediaPlayer.create(this, R.raw.sonido_puerta)
        GameManager.reiniciarVidas()

        val btnData: Button = findViewById(R.id.btnData)
        val btnVault: Button = findViewById(R.id.btnVault)
        val btnDoor: Button = findViewById(R.id.btnJaDoor5)
        val btnBack: Button = findViewById(R.id.btnBackJa5)
        val btnVolume: Button = findViewById(R.id.btnVolume)

        btnVolume.setOnClickListener { VolumeDialog.show(this) }

        btnData.setOnClickListener {
            val intent = Intent(this, SubLevelJavaActivity::class.java)
            intent.putExtra("SUBLEVEL", 1)
            intent.putExtra("NIVEL", 5)
            startActivityForResult(intent, 1)
            AnimManager.avanzar(this)
        }

        btnVault.setOnClickListener {
            if (!sublevel1Done) {
                Toast.makeText(this, "⚠️ Analiza los datos primero", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SubLevelJavaActivity::class.java)
                intent.putExtra("SUBLEVEL", 2)
                intent.putExtra("NIVEL", 5)
                startActivityForResult(intent, 2)
                AnimManager.avanzar(this)
            }
        }

        btnDoor.setOnClickListener {
            when {
                !sublevel1Done || !sublevel2Done -> {
                    Toast.makeText(this, "🔒 Desbloquea la bóveda primero", Toast.LENGTH_SHORT).show()
                }
                sublevel3Done -> mostrarVictoria()
                else -> {
                    val intent = Intent(this, SubLevelJavaActivity::class.java)
                    intent.putExtra("SUBLEVEL", 3)
                    intent.putExtra("NIVEL", 5)
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
                1 -> { sublevel1Done = true; actualizarProgreso(); Toast.makeText(this, "✅ Datos analizados!", Toast.LENGTH_SHORT).show() }
                2 -> { sublevel2Done = true; actualizarProgreso(); Toast.makeText(this, "✅ Bóveda abierta!", Toast.LENGTH_SHORT).show() }
                3 -> { sublevel3Done = true; actualizarProgreso(); mostrarVictoria() }
            }
        }
    }

    private fun actualizarProgreso() {
        val tvProgress: TextView = findViewById(R.id.tvJaProgress5)
        val s1 = if (sublevel1Done) "✅" else "[ ]"
        val s2 = if (sublevel2Done) "✅" else "[ ]"
        val s3 = if (sublevel3Done) "✅" else "[ ]"
        tvProgress.text = "PROGRESO: $s1 $s2 $s3"

        if (sublevel1Done && sublevel2Done) {
            val btnDoor: Button = findViewById(R.id.btnJaDoor5)
            val tvDoorStatus: TextView = findViewById(R.id.tvJaDoorStatus5)
            btnDoor.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#3A1A00"))
            tvDoorStatus.text = "INTENTA ABRIRLA"
            tvDoorStatus.setTextColor(android.graphics.Color.parseColor("#FFA500"))
        }
    }

    private fun mostrarVictoria() {
        playerPuerta?.start()
        val tvDoorStatus: TextView = findViewById(R.id.tvJaDoorStatus5)
        val btnDoor: Button = findViewById(R.id.btnJaDoor5)

        btnDoor.backgroundTintList = android.content.res.ColorStateList.valueOf(
            android.graphics.Color.parseColor("#00FF41"))
        btnDoor.setTextColor(android.graphics.Color.parseColor("#0A0A0A"))
        tvDoorStatus.text = "ABIERTA"
        tvDoorStatus.setTextColor(android.graphics.Color.parseColor("#00FF41"))

        val prefs = getSharedPreferences("CodeEscape", MODE_PRIVATE)
        prefs.edit().putBoolean("ja_nivel5_completo", true).apply()
        GameManager.nivelCompletado()
        GameManager.guardar(this)

        AlertDialog.Builder(this)
            .setTitle("🎉 ¡BÓVEDA SUPERADA!")
            .setMessage("Has completado el Nivel BYTECODE de Java.\n\n> Nivel JVM desbloqueado.")
            .setPositiveButton("CONTINUAR") { dialog, _ ->
                dialog.dismiss()
                finish()
                AnimManager.volver(this)
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerPuerta?.release()
    }
}