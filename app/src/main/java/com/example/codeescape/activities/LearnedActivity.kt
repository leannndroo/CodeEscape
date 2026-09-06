package com.example.codeescape.activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.MusicManager

class LearnedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_learned)

        val btnBack: Button = findViewById(R.id.btnBackLearned)
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
}