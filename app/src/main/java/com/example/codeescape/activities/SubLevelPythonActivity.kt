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
import com.example.codeescape.utils.GameManager
import com.example.codeescape.dialogs.VolumeDialog
import com.example.codeescape.utils.AnimManager
import com.example.codeescape.utils.MusicManager

class SubLevelPythonActivity : AppCompatActivity() {

    private var sublevelNumber = 0
    private var nivelNumber = 0
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
        nivelNumber = intent.getIntExtra("NIVEL", 1)

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
        when (nivelNumber) {
            1 -> when (sublevelNumber) {
                1 -> {
                    tvTitle.text = "SUBNIVEL 1 — LIBRO"
                    tvDesc.text = "El libro tiene instrucciones\npara mostrar mensajes."
                    tvQuestion.text = "¿Cómo se imprime 'Hola Mundo' en Python?"
                    tvHint.text = "💡 Usa print() con el texto entre comillas"
                    pistaExtraTexto = "Escribe: print('Hola Mundo')"
                }
                2 -> {
                    tvTitle.text = "SUBNIVEL 2 — LAPTOP"
                    tvDesc.text = "La laptop guarda información\nen variables."
                    tvQuestion.text = "¿Cómo se crea una variable\nllamada 'nombre' con valor 'Ana'?"
                    tvHint.text = "💡 Escribe el nombre, signo igual y el valor"
                    pistaExtraTexto = "Escribe: nombre = 'Ana'"
                }
                3 -> {
                    tvTitle.text = "SUBNIVEL 3 — PUERTA"
                    tvDesc.text = "Combina variable e impresión\npara abrir la puerta."
                    tvQuestion.text = "Crea una variable 'clave' con valor\n'OPEN' e imprímela"
                    tvHint.text = "💡 Primero crea la variable, luego usa print()"
                    pistaExtraTexto = "Escribe: clave = 'OPEN' y luego print(clave)"
                }
                else -> {}
            }
            2 -> when (sublevelNumber) {
                1 -> {
                    tvTitle.text = "SUBNIVEL 1 — PIZARRA"
                    tvDesc.text = "La pizarra muestra cómo\ndecidir entre opciones."
                    tvQuestion.text = "¿Cómo se escribe un if en Python\nque imprima 'si' si x es mayor a 3?"
                    tvHint.text = "💡 Usa if seguido de la condición y dos puntos"
                    pistaExtraTexto = "Escribe: if x > 3: print('si')"
                }
                2 -> {
                    tvTitle.text = "SUBNIVEL 2 — TERMINAL"
                    tvDesc.text = "El terminal repite\ntareas automáticamente."
                    tvQuestion.text = "¿Cómo se escribe un for en Python\nque vaya del 1 al 3?"
                    tvHint.text = "💡 Usa for con range()"
                    pistaExtraTexto = "Escribe: for i in range(1, 4):"
                }
                3 -> {
                    tvTitle.text = "SUBNIVEL 3 — PUERTA"
                    tvDesc.text = "La puerta necesita una función\npara abrirse."
                    tvQuestion.text = "¿Cómo se declara una función\nllamada 'abrir' en Python?"
                    tvHint.text = "💡 Usa def seguido del nombre y paréntesis"
                    pistaExtraTexto = "Escribe: def abrir():"
                }
                else -> {}
            }
            3 -> when (sublevelNumber) {
                1 -> {
                    tvTitle.text = "SUBNIVEL 1 — CAJA"
                    tvDesc.text = "La caja guarda múltiples\nvalores juntos."
                    tvQuestion.text = "¿Cómo se crea una lista\ncon los valores 1, 2, 3 en Python?"
                    tvHint.text = "💡 Usa corchetes con los valores separados por comas"
                    pistaExtraTexto = "Escribe: [1, 2, 3]"
                }
                2 -> {
                    tvTitle.text = "SUBNIVEL 2 — DRONE"
                    tvDesc.text = "El drone analiza datos\ncon diccionarios."
                    tvQuestion.text = "¿Cómo se crea un diccionario\ncon clave 'nombre' y valor 'Ana'?"
                    tvHint.text = "💡 Usa llaves con clave y valor separados por dos puntos"
                    pistaExtraTexto = "Escribe: {'nombre': 'Ana'}"
                }
                3 -> {
                    tvTitle.text = "SUBNIVEL 3 — PUERTA FINAL"
                    tvDesc.text = "La puerta final necesita\nque manejes errores."
                    tvQuestion.text = "¿Cómo se escribe un try except\nen Python para manejar errores?"
                    tvHint.text = "💡 Usa try: en una línea y except: en la siguiente"
                    pistaExtraTexto = "Escribe: try: ... except:"
                }
                else -> {}
            }
            4 -> when (sublevelNumber) {
                1 -> {
                    tvTitle.text = "SUBNIVEL 1 — INIT"
                    tvDesc.text = "El objeto nace con el\nmétodo constructor."
                    tvQuestion.text = "¿Cómo se define el constructor\n__init__ en Python?"
                    tvHint.text = "💡 Usa def __init__(self):"
                    pistaExtraTexto = "Escribe: def __init__(self):"
                }
                2 -> {
                    tvTitle.text = "SUBNIVEL 2 — SELF"
                    tvDesc.text = "El objeto se refiere a sí mismo."
                    tvQuestion.text = "¿Qué palabra se usa como primer\nparámetro en los métodos?"
                    tvHint.text = "💡 Empieza con 's'"
                    pistaExtraTexto = "Escribe: self"
                }
                3 -> {
                    tvTitle.text = "SUBNIVEL 3 — CLASE"
                    tvDesc.text = "Creando el molde del objeto."
                    tvQuestion.text = "¿Cómo se declara una clase\nllamada 'Robot' en Python?"
                    tvHint.text = "💡 Usa class Robot:"
                    pistaExtraTexto = "Escribe: class Robot:"
                }
                else -> {}
            }
            5 -> when (sublevelNumber) {
                1 -> {
                    tvTitle.text = "SUBNIVEL 1 — HERENCIA"
                    tvDesc.text = "Un Guerrero es un Humano."
                    tvQuestion.text = "¿Cómo hereda Guerrero de Humano\nen Python?"
                    tvHint.text = "💡 Usa Guerrero(Humano):"
                    pistaExtraTexto = "Escribe: class Guerrero(Humano):"
                }
                2 -> {
                    tvTitle.text = "SUBNIVEL 2 — SUPER"
                    tvDesc.text = "Llamando al padre."
                    tvQuestion.text = "¿Cómo llamas al método del padre?"
                    tvHint.text = "💡 Usa super()"
                    pistaExtraTexto = "Escribe: super().__init__()"
                }
                3 -> {
                    tvTitle.text = "SUBNIVEL 3 — MÉTODOS"
                    tvDesc.text = "Acciones del objeto."
                    tvQuestion.text = "Define un método 'atacar'\nque reciba self."
                    tvHint.text = "💡 Usa def atacar(self):"
                    pistaExtraTexto = "Escribe: def atacar(self):"
                }
                else -> {}
            }
            6 -> when (sublevelNumber) {
                1 -> {
                    tvTitle.text = "SUBNIVEL 1 — IMPORT"
                    tvDesc.text = "Usando herramientas externas."
                    tvQuestion.text = "¿Cómo importas la librería 'math'?"
                    tvHint.text = "💡 Usa la palabra import"
                    pistaExtraTexto = "Escribe: import math"
                }
                2 -> {
                    tvTitle.text = "SUBNIVEL 2 — FROM"
                    tvDesc.text = "Importando algo específico."
                    tvQuestion.text = "¿Cómo importas 'sqrt' de 'math'?"
                    tvHint.text = "💡 Usa from ... import ..."
                    pistaExtraTexto = "Escribe: from math import sqrt"
                }
                3 -> {
                    tvTitle.text = "SUBNIVEL 3 — AS"
                    tvDesc.text = "Dando un apodo a la librería."
                    tvQuestion.text = "¿Cómo importas 'pandas' como 'pd'?"
                    tvHint.text = "💡 Usa la palabra as"
                    pistaExtraTexto = "Escribe: import pandas as pd"
                }
                else -> {}
            }
            7 -> when (sublevelNumber) {
                1 -> {
                    tvTitle.text = "SUBNIVEL 1 — COMPREHENSION"
                    tvDesc.text = "Listas en una sola línea."
                    tvQuestion.text = "Crea una lista de x para x en\nrange(5) usando comprehension"
                    tvHint.text = "💡 Usa [x for x in range(5)]"
                    pistaExtraTexto = "Escribe: [x for x in range(5)]"
                }
                2 -> {
                    tvTitle.text = "SUBNIVEL 2 — LAMBDA"
                    tvDesc.text = "Funciones anónimas rápidas."
                    tvQuestion.text = "Crea una función lambda que\nsume 10 a un argumento 'a'"
                    tvHint.text = "💡 Usa lambda a: a + 10"
                    pistaExtraTexto = "Escribe: lambda a : a + 10"
                }
                3 -> {
                    tvTitle.text = "SUBNIVEL 3 — FINAL BOSS"
                    tvDesc.text = "El último reto de Python."
                    tvQuestion.text = "¿Cómo obtienes la longitud de\nuna lista en Python?"
                    tvHint.text = "💡 Usa la función len()"
                    pistaExtraTexto = "Escribe: len(lista)"
                }
                else -> {}
            }
        }
    }

    private fun verificarRespuesta(respuesta: String): Boolean {
        return when (nivelNumber) {
            1 -> when (sublevelNumber) {
                1 -> respuesta.contains("print") && respuesta.contains("hola mundo")
                2 -> respuesta.contains("nombre") && respuesta.contains("=") &&
                        respuesta.contains("ana")
                3 -> respuesta.contains("clave") && respuesta.contains("print")
                else -> false
            }
            2 -> when (sublevelNumber) {
                1 -> respuesta.contains("if") && respuesta.contains("3") &&
                        respuesta.contains("si")
                2 -> respuesta.contains("for") &&
                        (respuesta.contains("range") || respuesta.contains("1"))
                3 -> respuesta.contains("def") && respuesta.contains("abrir")
                else -> false
            }
            3 -> when (sublevelNumber) {
                1 -> respuesta.contains("[") && respuesta.contains("1") &&
                        respuesta.contains("2") && respuesta.contains("3")
                2 -> respuesta.contains("{") && respuesta.contains("nombre") &&
                        respuesta.contains("ana")
                3 -> respuesta.contains("try") && respuesta.contains("except")
                else -> false
            }
            4 -> when (sublevelNumber) {
                1 -> respuesta.contains("def") && respuesta.contains("__init__")
                2 -> respuesta == "self" || respuesta.contains("self")
                3 -> respuesta.contains("class") && respuesta.contains("robot")
                else -> false
            }
            5 -> when (sublevelNumber) {
                1 -> respuesta.contains("class") && respuesta.contains("guerrero") && respuesta.contains("humano")
                2 -> respuesta.contains("super")
                3 -> respuesta.contains("def") && respuesta.contains("atacar")
                else -> false
            }
            6 -> when (sublevelNumber) {
                1 -> respuesta.contains("import") && respuesta.contains("math")
                2 -> respuesta.contains("from") && respuesta.contains("math") && respuesta.contains("sqrt")
                3 -> respuesta.contains("import") && respuesta.contains("pandas") && respuesta.contains("as") && respuesta.contains("pd")
                else -> false
            }
            7 -> when (sublevelNumber) {
                1 -> respuesta.contains("[") && respuesta.contains("for") && respuesta.contains("in") && respuesta.contains("range")
                2 -> respuesta.contains("lambda") && respuesta.contains("+")
                3 -> respuesta.contains("len(")
                else -> false
            }
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