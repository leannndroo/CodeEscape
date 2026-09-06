package com.example.codeescape.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.MusicManager

class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_tutorial)

        val btnSkip: Button = findViewById(R.id.btnSkip)
        val btnStartGame: Button = findViewById(R.id.btnStartGame)

        btnSkip.setOnClickListener { irASeleccionMundo() }
        btnStartGame.setOnClickListener { irASeleccionMundo() }
    }

    private fun irASeleccionMundo() {
        startActivity(Intent(this, WorldSelectActivity::class.java))
        AnimManager.avanzar(this)
        finish()
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