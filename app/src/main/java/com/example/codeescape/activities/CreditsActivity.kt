package com.example.codeescape.activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.MusicManager

class CreditsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_credits)

        val btnBack: Button = findViewById(R.id.btnBackCredits)
        val tvDeveloper: TextView = findViewById(R.id.tvDeveloper)
        val tvSubject: TextView = findViewById(R.id.tvSubject)
        val tvInstitution: TextView = findViewById(R.id.tvInstitution)
        val tvYear: TextView = findViewById(R.id.tvYear)

        tvDeveloper.text = "Leandro Castelblanco\nMiguel Garzón\nBrayan Bernal"
        tvSubject.text = "PROGRAMACIÓN MÓVIL"
        tvInstitution.text = "TEINCO"
        tvYear.text = "2026"

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