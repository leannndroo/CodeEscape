package com.example.codeescape.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.dialogs.VolumeDialog
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.MusicManager

class WorldSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_world_select)

        // Ahora son Button en el XML
        val btnPython: Button = findViewById(R.id.btnPython)
        val btnJava: Button = findViewById(R.id.btnJava)
        val btnKotlin: Button = findViewById(R.id.btnKotlin)
        val btnBack: Button = findViewById(R.id.btnBack)
        val btnVolume: Button = findViewById(R.id.btnVolume)
        val tvPythonStatus: TextView = findViewById(R.id.tvPythonStatus)
        val tvJavaStatus: TextView = findViewById(R.id.tvJavaStatus)
        val tvKotlinStatus: TextView = findViewById(R.id.tvKotlinStatus)

        val prefs = getSharedPreferences("CodeEscape", MODE_PRIVATE)

        // Python completo cuando termina nivel 7 de Python
        if (prefs.getBoolean("py_nivel7_completo", false)) {
            tvPythonStatus.text = "✅ COMPLETADO"
            tvPythonStatus.setTextColor(android.graphics.Color.parseColor("#00FF41"))
            btnPython.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#003A00"))
        }

        // Java completo cuando termina nivel 7 de Java
        if (prefs.getBoolean("ja_nivel7_completo", false)) {
            tvJavaStatus.text = "✅ COMPLETADO"
            tvJavaStatus.setTextColor(android.graphics.Color.parseColor("#00FF41"))
            btnJava.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#3A2A00"))
        }

        // Kotlin completo cuando termina nivel 7 de Kotlin
        if (prefs.getBoolean("kt_nivel7_completo", false)) {
            tvKotlinStatus.text = "✅ COMPLETADO"
            tvKotlinStatus.setTextColor(android.graphics.Color.parseColor("#00FF41"))
            btnKotlin.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#1A003A"))
        }

        btnPython.setOnClickListener {
            startActivity(Intent(this, LevelSelectPythonActivity::class.java))
            AnimManager.avanzar(this)
        }

        btnJava.setOnClickListener {
            startActivity(Intent(this, LevelSelectJavaActivity::class.java))
            AnimManager.avanzar(this)
        }

        btnKotlin.setOnClickListener {
            startActivity(Intent(this, LevelSelectActivity::class.java))
            AnimManager.avanzar(this)
        }

        btnBack.setOnClickListener {
            finish()
            AnimManager.volver(this)
        }

        btnVolume.setOnClickListener { VolumeDialog.show(this) }
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