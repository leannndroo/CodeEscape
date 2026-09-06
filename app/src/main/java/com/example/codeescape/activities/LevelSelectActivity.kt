package com.example.codeescape.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.dialogs.VolumeDialog
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.MusicManager

class LevelSelectActivity : AppCompatActivity() {

    private val nombres = listOf(
        "INICIADO 🟢",
        "GUERRERO ⚔️",
        "DEMENCIAL 💀",
        "HACKERMAN 🔧",
        "CORRUPTO 🌀",
        "APOCALIPSIS ☠️",
        "DIOS DEL CÓDIGO 👑"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_level_select_7)

        val btnBack: Button = findViewById(R.id.btnBack)
        val btnVolume: Button = findViewById(R.id.btnVolume)

        findViewById<TextView>(R.id.tvLevelSelectTitle).text = "🤖 KOTLIN — SELECCIONA NIVEL"

        findViewById<TextView>(R.id.tvLevel1Name).text = nombres[0]
        findViewById<TextView>(R.id.tvLevel2Name).text = nombres[1]
        findViewById<TextView>(R.id.tvLevel3Name).text = nombres[2]
        findViewById<TextView>(R.id.tvLevel4Name).text = nombres[3]
        findViewById<TextView>(R.id.tvLevel5Name).text = nombres[4]
        findViewById<TextView>(R.id.tvLevel6Name).text = nombres[5]
        findViewById<TextView>(R.id.tvLevel7Name).text = nombres[6]

        btnBack.setOnClickListener {
            finish()
            AnimManager.volver(this)
        }
        btnVolume.setOnClickListener { VolumeDialog.show(this) }
    }

    override fun onResume() {
        super.onResume()
        MusicManager.iniciar(this)
        actualizarNiveles()
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pausar()
    }

    private fun actualizarNiveles() {
        val prefs = getSharedPreferences("CodeEscape", MODE_PRIVATE)

        // Niveles 1-3 usan claves "nivel1_completo" (originales de Level1Activity)
        // Niveles 4-7 usan claves "kt_nivel4_completo" (nuevas de Level4KotlinActivity)
        val completados = listOf(
            true,
            prefs.getBoolean("nivel1_completo", false),
            prefs.getBoolean("nivel2_completo", false),
            prefs.getBoolean("kt_nivel4_completo", false),
            prefs.getBoolean("kt_nivel5_completo", false),
            prefs.getBoolean("kt_nivel6_completo", false),
            prefs.getBoolean("kt_nivel7_completo", false) // nivel 3 desbloquea el 4
        )

        // Nota: nivel3_completo desbloquea nivel 4 de Kotlin
        // Reemplazamos el índice 3 con nivel3_completo para el desbloqueo correcto
        val completadosCorregidos = listOf(
            true,
            prefs.getBoolean("nivel1_completo", false),  // desbloquea nivel 2
            prefs.getBoolean("nivel2_completo", false),  // desbloquea nivel 3
            prefs.getBoolean("nivel3_completo", false),  // desbloquea nivel 4
            prefs.getBoolean("kt_nivel4_completo", false), // desbloquea nivel 5
            prefs.getBoolean("kt_nivel5_completo", false), // desbloquea nivel 6
            prefs.getBoolean("kt_nivel6_completo", false)  // desbloquea nivel 7
        )

        val statusViews = listOf(
            R.id.tvLevel1Status, R.id.tvLevel2Status, R.id.tvLevel3Status,
            R.id.tvLevel4Status, R.id.tvLevel5Status, R.id.tvLevel6Status,
            R.id.tvLevel7Status
        )

        val nameViews = listOf(
            R.id.tvLevel1Name, R.id.tvLevel2Name, R.id.tvLevel3Name,
            R.id.tvLevel4Name, R.id.tvLevel5Name, R.id.tvLevel6Name,
            R.id.tvLevel7Name
        )

        val lockViews = listOf(
            R.id.tvLevel1Lock, R.id.tvLevel2Lock, R.id.tvLevel3Lock,
            R.id.tvLevel4Lock, R.id.tvLevel5Lock, R.id.tvLevel6Lock,
            R.id.tvLevel7Lock
        )

        val btnIds = listOf(
            R.id.btnLevel1, R.id.btnLevel2, R.id.btnLevel3, R.id.btnLevel4,
            R.id.btnLevel5, R.id.btnLevel6, R.id.btnLevel7
        )

        val activities = listOf(
            Level1Activity::class.java,
            Level2Activity::class.java,
            Level3Activity::class.java,
            Level4KotlinActivity::class.java,
            Level5KotlinActivity::class.java,
            Level6KotlinActivity::class.java,
            Level7KotlinActivity::class.java
        )

        for (i in 0..6) {
            val desbloqueado = completadosCorregidos[i]
            val tvStatus = findViewById<TextView>(statusViews[i])
            val tvName = findViewById<TextView>(nameViews[i])
            val tvLock = findViewById<TextView>(lockViews[i])
            val btn = findViewById<View>(btnIds[i])

            if (desbloqueado) {
                tvLock.visibility = View.GONE
                tvStatus.text = "✅"
                tvStatus.setTextColor(android.graphics.Color.parseColor("#00FF41"))
                tvName.setTextColor(android.graphics.Color.parseColor("#00FF41"))
            } else {
                tvLock.visibility = View.VISIBLE
                tvStatus.text = "🔒"
                tvStatus.setTextColor(android.graphics.Color.parseColor("#FF0000"))
                tvName.setTextColor(android.graphics.Color.parseColor("#555555"))
            }

            btn.setOnClickListener {
                if (!desbloqueado) {
                    Toast.makeText(
                        this,
                        "🔒 Completa el nivel anterior primero",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    startActivity(Intent(this, activities[i]))
                    AnimManager.avanzar(this)
                }
            }
        }
    }
}