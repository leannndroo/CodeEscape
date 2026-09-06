package com.example.codeescape.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.GameManager
import com.example.codeescape.utils.MusicManager

class StatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_stats)

        val tvRango: TextView = findViewById(R.id.tvRango)
        val tvPuntosStats: TextView = findViewById(R.id.tvPuntosStats)
        val tvMejorPuntaje: TextView = findViewById(R.id.tvMejorPuntaje)
        val tvCorrectas: TextView = findViewById(R.id.tvCorrectas)
        val tvIncorrectas: TextView = findViewById(R.id.tvIncorrectas)
        val tvNiveles: TextView = findViewById(R.id.tvNiveles)
        val tvPistas: TextView = findViewById(R.id.tvPistas)
        val tvPythonProgress: TextView = findViewById(R.id.tvPythonProgress)
        val tvJavaProgress: TextView = findViewById(R.id.tvJavaProgress)
        val tvKotlinProgress: TextView = findViewById(R.id.tvKotlinProgress)
        val btnVerAprendido: Button = findViewById(R.id.btnVerAprendido)
        val btnBackStats: Button = findViewById(R.id.btnBackStats)

        GameManager.cargar(this)
        val prefs = getSharedPreferences("CodeEscape", MODE_PRIVATE)
        val mejorPuntaje = prefs.getInt("mejor_puntaje", 0)

        tvRango.text = "🏆 RANGO: ${GameManager.obtenerRango()}"
        tvPuntosStats.text = "⭐ Puntos: ${GameManager.puntosTotal}"
        tvMejorPuntaje.text = "🥇 Mejor puntaje: $mejorPuntaje"
        tvCorrectas.text = "✅ Correctas: ${GameManager.respuestasCorrectas}"
        tvIncorrectas.text = "❌ Incorrectas: ${GameManager.respuestasIncorrectas}"
        tvNiveles.text = "🎮 Niveles: ${GameManager.nivelesCompletados}/21"
        tvPistas.text = "💡 Pistas usadas: ${GameManager.pistasUsadas}"

        val py1 = if (prefs.getBoolean("py_nivel1_completo", false)) "✅" else "[ ]"
        val py2 = if (prefs.getBoolean("py_nivel2_completo", false)) "✅" else "[ ]"
        val py3 = if (prefs.getBoolean("py_nivel3_completo", false)) "✅" else "[ ]"
        val py4 = if (prefs.getBoolean("py_nivel4_completo", false)) "✅" else "[ ]"
        val py5 = if (prefs.getBoolean("py_nivel5_completo", false)) "✅" else "[ ]"
        val py6 = if (prefs.getBoolean("py_nivel6_completo", false)) "✅" else "[ ]"
        val py7 = if (prefs.getBoolean("py_nivel7_completo", false)) "✅" else "[ ]"
        tvPythonProgress.text = "$py1$py2$py3$py4$py5$py6$py7"

        val ja1 = if (prefs.getBoolean("ja_nivel1_completo", false)) "✅" else "[ ]"
        val ja2 = if (prefs.getBoolean("ja_nivel2_completo", false)) "✅" else "[ ]"
        val ja3 = if (prefs.getBoolean("ja_nivel3_completo", false)) "✅" else "[ ]"
        val ja4 = if (prefs.getBoolean("ja_nivel4_completo", false)) "✅" else "[ ]"
        val ja5 = if (prefs.getBoolean("ja_nivel5_completo", false)) "✅" else "[ ]"
        val ja6 = if (prefs.getBoolean("ja_nivel6_completo", false)) "✅" else "[ ]"
        val ja7 = if (prefs.getBoolean("ja_nivel7_completo", false)) "✅" else "[ ]"
        tvJavaProgress.text = "$ja1$ja2$ja3$ja4$ja5$ja6$ja7"

        val kt1 = if (prefs.getBoolean("nivel1_completo", false)) "✅" else "[ ]"
        val kt2 = if (prefs.getBoolean("nivel2_completo", false)) "✅" else "[ ]"
        val kt3 = if (prefs.getBoolean("nivel3_completo", false)) "✅" else "[ ]"
        val kt4 = if (prefs.getBoolean("nivel4_completo", false)) "✅" else "[ ]"
        val kt5 = if (prefs.getBoolean("nivel5_completo", false)) "✅" else "[ ]"
        val kt6 = if (prefs.getBoolean("nivel6_completo", false)) "✅" else "[ ]"
        val kt7 = if (prefs.getBoolean("nivel7_completo", false)) "✅" else "[ ]"
        tvKotlinProgress.text = "$kt1$kt2$kt3$kt4$kt5$kt6$kt7"

        btnVerAprendido.setOnClickListener {
            startActivity(Intent(this, LearnedActivity::class.java))
            AnimManager.avanzar(this)
        }

        btnBackStats.setOnClickListener {
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
}