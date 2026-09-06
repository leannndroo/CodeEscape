package com.example.codeescape.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.codeescape.R
import com.example.codeescape.dialogs.VolumeDialog
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.GameManager
import com.example.codeescape.utils.MusicManager

class SubLevelActivity : AppCompatActivity() {

    private var sublevelNumber = 0
    private var playerExito: MediaPlayer? = null
    private var playerError: MediaPlayer? = null
    private var timer: CountDownTimer? = null
    private var segundosTranscurridos = 0L
    private var pistaExtraUsada = false
    private var pistaExtraTexto = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_sublevel)

        playerExito = MediaPlayer.create(this, R.raw.sonido_exito)
        playerError = MediaPlayer.create(this, R.raw.sonido_error)

        val tvTitle: TextView = findViewById(R.id.tvSublevelTitle)
        val tvDesc: TextView = findViewById(R.id.tvSublevelDesc)
        val tvQuestion: TextView = findViewById(R.id.tvSublevelQuestion)
        val tvHint: TextView = findViewById(R.id.tvHint)
        val tvFeedback: TextView = findViewById(R.id.tvFeedback)
        val tvVidas: TextView = findViewById(R.id.tvVidas)
        val tvPuntos: TextView = findViewById(R.id.tvPuntos)
        val tvTimer: TextView = findViewById(R.id.tvTimer)
        val tvPistaExtra: TextView = findViewById(R.id.tvPistaExtra)
        val etAnswer: EditText = findViewById(R.id.etAnswer)
        val btnCheck: Button = findViewById(R.id.btnCheck)
        val btnBack: Button = findViewById(R.id.btnBackSub)
        val btnVolume: Button = findViewById(R.id.btnVolume)
        val btnPistaExtra: Button = findViewById(R.id.btnPistaExtra)

        sublevelNumber = intent.getIntExtra("SUBLEVEL", 1)

        actualizarVidas(tvVidas)
        tvPuntos.text = "⭐ ${GameManager.puntosTotal} pts"

        cargarPregunta(tvTitle, tvDesc, tvQuestion, tvHint)
        iniciarTimer(tvTimer)

        btnVolume.setOnClickListener { VolumeDialog.show(this) }

        btnPistaExtra.setOnClickListener {
            if (!pistaExtraUsada) {
                pistaExtraUsada = true
                GameManager.usarPista()
                tvPuntos.text = "⭐ ${GameManager.puntosTotal} pts"
                tvPistaExtra.text = "💡 $pistaExtraTexto"
                tvPistaExtra.visibility = View.VISIBLE
                btnPistaExtra.isEnabled = false
                btnPistaExtra.backgroundTintList =
                    android.content.res.ColorStateList.valueOf(
                        android.graphics.Color.parseColor("#1A1A1A"))
                btnPistaExtra.text = "💡 PISTA USADA"
            }
        }

        btnCheck.setOnClickListener {
            val respuesta = etAnswer.text.toString().trim().lowercase()
            val esCorrecta = verificarRespuesta(respuesta)

            if (esCorrecta) {
                timer?.cancel()
                playerExito?.start()
                GameManager.respuestaCorrecta(segundosTranscurridos)
                tvPuntos.text = "⭐ ${GameManager.puntosTotal} pts"
                tvFeedback.text = "✅ ¡CORRECTO! +${GameManager.PUNTOS_CORRECTO}pts"
                tvFeedback.setTextColor(android.graphics.Color.parseColor("#00FF41"))
                btnCheck.postDelayed({
                    setResult(RESULT_OK)
                    finish()
                }, 1000)
            } else {
                playerError?.start()
                val quedanVidas = GameManager.respuestaIncorrecta()
                actualizarVidas(tvVidas)
                if (!quedanVidas) {
                    timer?.cancel()
                    mostrarGameOver()
                } else {
                    tvFeedback.text = "❌ ERROR — ${GameManager.vidasActuales} vidas restantes"
                    tvFeedback.setTextColor(android.graphics.Color.parseColor("#FF0000"))
                    etAnswer.text.clear()
                }
            }
        }

        btnBack.setOnClickListener {
            timer?.cancel()
            setResult(RESULT_CANCELED)
            finish()
            AnimManager.volver(this)
        }
    }

    private fun iniciarTimer(tvTimer: TextView) {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                segundosTranscurridos++
                val minutos = segundosTranscurridos / 60
                val segundos = segundosTranscurridos % 60
                tvTimer.text = "⏱️ %02d:%02d".format(minutos, segundos)
                if (segundosTranscurridos > 30) {
                    tvTimer.setTextColor(android.graphics.Color.parseColor("#FF0000"))
                } else if (segundosTranscurridos > 15) {
                    tvTimer.setTextColor(android.graphics.Color.parseColor("#FFA500"))
                }
            }
            override fun onFinish() {}
        }.start()
    }

    private fun actualizarVidas(tvVidas: TextView) {
        tvVidas.text = when (GameManager.vidasActuales) {
            3 -> "❤️❤️❤️"
            2 -> "❤️❤️🖤"
            1 -> "❤️🖤🖤"
            else -> "🖤🖤🖤"
        }
    }

    private fun mostrarGameOver() {
        AlertDialog.Builder(this)
            .setTitle("💀 SIN VIDAS")
            .setMessage("Te quedaste sin vidas.\nEl nivel se reiniciará.")
            .setPositiveButton("REINTENTAR") { dialog, _ ->
                GameManager.reiniciarVidas()
                dialog.dismiss()
                setResult(RESULT_CANCELED)
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun cargarPregunta(
        tvTitle: TextView,
        tvDesc: TextView,
        tvQuestion: TextView,
        tvHint: TextView
    ) {
        when (sublevelNumber) {
            1 -> {
                tvTitle.text = "SUBNIVEL 1 — TELEVISOR"
                tvDesc.text = "El televisor muestra un mensaje extraño.\nNecesitas imprimirlo en pantalla."
                tvQuestion.text = "¿Cómo se imprime 'Hola Mundo' en Kotlin?"
                tvHint.text = "💡 Usa println() con el texto entre comillas"
                pistaExtraTexto = "Escribe: println(\"Hola Mundo\")"
            }
            2 -> {
                tvTitle.text = "SUBNIVEL 2 — COMPUTADOR"
                tvDesc.text = "El computador pide una clave.\nDebes declararla como variable."
                tvQuestion.text = "¿Cómo se declara una variable\nllamada 'clave' con valor 1234 en Kotlin?"
                tvHint.text = "💡 Usa val o var seguido del nombre"
                pistaExtraTexto = "Escribe: val clave = 1234"
            }
            3 -> {
                tvTitle.text = "SUBNIVEL 3 — PUERTA FINAL"
                tvDesc.text = "Combina lo que aprendiste\npara abrir la puerta."
                tvQuestion.text = "Declara una variable 'codigo' con valor\n'ESCAPE' e imprímela"
                tvHint.text = "💡 Usa println(codigo) o println(\"ESCAPE\")"
                pistaExtraTexto = "Escribe: println(\"ESCAPE\")"
            }
        }
    }

    private fun verificarRespuesta(respuesta: String): Boolean {
        return when (sublevelNumber) {
            1 -> respuesta.contains("println") && respuesta.contains("hola mundo")
            2 -> (respuesta.contains("val") || respuesta.contains("var")) &&
                    respuesta.contains("clave") && respuesta.contains("1234")
            3 -> respuesta.contains("println") &&
                    (respuesta.contains("codigo") || respuesta.contains("escape"))
            else -> false
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

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        playerExito?.release()
        playerError?.release()
    }
}