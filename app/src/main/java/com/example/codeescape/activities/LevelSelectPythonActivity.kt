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

class LevelSelectPythonActivity : AppCompatActivity() {

    private val nombres = listOf(
        "SERPIENTE 🐍",
        "ANACONDA 🔥",
        "PITÓN 💀",
        "COBRA ⚡",
        "VÍBORA 🌀",
        "MAMBA ☠️",
        "PITÓN REY 👑"
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

        findViewById<TextView>(R.id.tvLevelSelectTitle).text = "🐍 PYTHON — SELECCIONA NIVEL"

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

        val completados = listOf(
            true,
            prefs.getBoolean("py_nivel1_completo", false),
            prefs.getBoolean("py_nivel2_completo", false),
            prefs.getBoolean("py_nivel3_completo", false),
            prefs.getBoolean("py_nivel4_completo", false),
            prefs.getBoolean("py_nivel5_completo", false),
            prefs.getBoolean("py_nivel6_completo", false)
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

        // IDs del candado dentro de cada puerta
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
            Level1PythonActivity::class.java,
            Level2PythonActivity::class.java,
            Level3PythonActivity::class.java,
            Level4PythonActivity::class.java,
            Level5PythonActivity::class.java,
            Level6PythonActivity::class.java,
            Level7PythonActivity::class.java
        )

        for (i in 0..6) {
            val desbloqueado = completados[i]
            val tvStatus = findViewById<TextView>(statusViews[i])
            val tvName = findViewById<TextView>(nameViews[i])
            val tvLock = findViewById<TextView>(lockViews[i])
            val btn = findViewById<View>(btnIds[i])

            if (desbloqueado) {
                // Ocultar candado de la puerta
                tvLock.visibility = View.GONE
                // Actualizar textos
                tvStatus.text = "✅"
                tvStatus.setTextColor(android.graphics.Color.parseColor("#00FF41"))
                tvName.setTextColor(android.graphics.Color.parseColor("#00FF41"))
            } else {
                // Mostrar candado de la puerta
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