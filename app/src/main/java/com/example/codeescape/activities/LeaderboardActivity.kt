package com.example.codeescape.activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.GameManager
import com.example.codeescape.utils.MusicManager

class LeaderboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_leaderboard)

        val btnBack: Button = findViewById(R.id.btnBackLeader)
        val btnReset: Button = findViewById(R.id.btnResetLeader)

        cargarPuntajes()

        btnBack.setOnClickListener {
            finish()
            AnimManager.volver(this)
        }

        btnReset.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("🗑️ RESETEAR TABLA")
                .setMessage("¿Estás seguro que quieres borrar\ntodos los puntajes?")
                .setPositiveButton("SÍ, BORRAR") { dialog, _ ->
                    resetearPuntajes()
                    cargarPuntajes()
                    dialog.dismiss()
                }
                .setNegativeButton("CANCELAR") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun cargarPuntajes() {
        val prefs = getSharedPreferences("Leaderboard", MODE_PRIVATE)

        val nombres = listOf(
            findViewById<TextView>(R.id.tvName1),
            findViewById<TextView>(R.id.tvName2),
            findViewById<TextView>(R.id.tvName3),
            findViewById<TextView>(R.id.tvName4),
            findViewById<TextView>(R.id.tvName5)
        )
        val puntajes = listOf(
            findViewById<TextView>(R.id.tvScore1),
            findViewById<TextView>(R.id.tvScore2),
            findViewById<TextView>(R.id.tvScore3),
            findViewById<TextView>(R.id.tvScore4),
            findViewById<TextView>(R.id.tvScore5)
        )
        val rangos = listOf(
            findViewById<TextView>(R.id.tvRank1),
            findViewById<TextView>(R.id.tvRank2),
            findViewById<TextView>(R.id.tvRank3),
            findViewById<TextView>(R.id.tvRank4),
            findViewById<TextView>(R.id.tvRank5)
        )

        for (i in 1..5) {
            val nombre = prefs.getString("nombre_$i", "---") ?: "---"
            val puntaje = prefs.getInt("puntaje_$i", 0)
            val rango = prefs.getString("rango_$i", "---") ?: "---"

            nombres[i - 1].text = nombre
            puntajes[i - 1].text = if (puntaje > 0) "$puntaje pts" else "---"
            rangos[i - 1].text = rango
        }
    }

    companion object {
        fun guardarPuntaje(context: android.content.Context, nombre: String, puntaje: Int) {
            val prefs = context.getSharedPreferences("Leaderboard", android.content.Context.MODE_PRIVATE)
            val puntajesActuales = mutableListOf<Pair<String, Int>>()

            for (i in 1..5) {
                val n = prefs.getString("nombre_$i", "---") ?: "---"
                val p = prefs.getInt("puntaje_$i", 0)
                if (p > 0) puntajesActuales.add(Pair(n, p))
            }

            val rango = GameManager.obtenerRango()
            puntajesActuales.add(Pair(nombre, puntaje))
            puntajesActuales.sortByDescending { it.second }

            val editor = prefs.edit()
            for (i in 1..5) {
                if (i <= puntajesActuales.size) {
                    editor.putString("nombre_$i", puntajesActuales[i - 1].first)
                    editor.putInt("puntaje_$i", puntajesActuales[i - 1].second)
                    editor.putString("rango_$i", rango)
                }
            }
            editor.apply()
        }
    }

    private fun resetearPuntajes() {
        getSharedPreferences("Leaderboard", MODE_PRIVATE).edit().clear().apply()
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