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

class SubLevelKotlinActivity : AppCompatActivity() {

    private var sublevelNumber = 0
    private var nivelNumber = 0
    private var playerExito: MediaPlayer? = null
    private var playerError: MediaPlayer? = null
    private var timer: CountDownTimer? = null
    private var segundosTranscurridos = 0L
    private var pistaExtraUsada = false
    private var respuestaCompleta = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
                tvPistaExtra.text = "✅ Respuesta: $respuestaCompleta"
                tvPistaExtra.visibility = View.VISIBLE
                tvPistaExtra.setTextColor(android.graphics.Color.parseColor("#FFE03E"))
                btnPistaExtra.isEnabled = false
                btnPistaExtra.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#1A1A1A"))
                btnPistaExtra.text = "💡 PISTA USADA (-25pts)"
            }
        }

        btnCheck.setOnClickListener {
            val respuesta = etAnswer.text.toString().trim().lowercase()
            if (verificarRespuesta(respuesta)) {
                timer?.cancel()
                playerExito?.start()
                GameManager.respuestaCorrecta(segundosTranscurridos)
                tvPuntos.text = "⭐ ${GameManager.puntosTotal} pts"
                tvFeedback.text = "✅ ¡CORRECTO! +${GameManager.PUNTOS_CORRECTO}pts"
                tvFeedback.setTextColor(android.graphics.Color.parseColor("#00FF41"))
                btnCheck.postDelayed({ setResult(RESULT_OK); finish() }, 1000)
            } else {
                playerError?.start()
                val quedanVidas = GameManager.respuestaIncorrecta()
                actualizarVidas(tvVidas)
                if (!quedanVidas) { timer?.cancel(); mostrarGameOver() }
                else {
                    tvFeedback.text = "❌ ERROR — ${GameManager.vidasActuales} vidas"
                    tvFeedback.setTextColor(android.graphics.Color.parseColor("#FF0000"))
                    etAnswer.text.clear()
                }
            }
        }

        btnBack.setOnClickListener {
            timer?.cancel(); setResult(RESULT_CANCELED); finish(); AnimManager.volver(this)
        }
    }

    private fun iniciarTimer(tvTimer: TextView) {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                segundosTranscurridos++
                tvTimer.text = "⏱️ %02d:%02d".format(segundosTranscurridos / 60, segundosTranscurridos % 60)
                if (segundosTranscurridos > 30) tvTimer.setTextColor(android.graphics.Color.parseColor("#FF0000"))
                else if (segundosTranscurridos > 15) tvTimer.setTextColor(android.graphics.Color.parseColor("#FFA500"))
            }
            override fun onFinish() {}
        }.start()
    }

    private fun actualizarVidas(tv: TextView) {
        tv.text = when (GameManager.vidasActuales) { 3 -> "❤️❤️❤️"; 2 -> "❤️❤️🖤"; 1 -> "❤️🖤🖤"; else -> "🖤🖤🖤" }
    }

    private fun mostrarGameOver() {
        AlertDialog.Builder(this).setTitle("💀 SIN VIDAS").setMessage("Te quedaste sin vidas.\nEl nivel se reiniciará.")
            .setPositiveButton("REINTENTAR") { d, _ -> GameManager.reiniciarVidas(); d.dismiss(); setResult(RESULT_CANCELED); finish() }
            .setCancelable(false).show()
    }

    private fun cargarPregunta(tvTitle: TextView, tvDesc: TextView, tvQuestion: TextView, tvHint: TextView) {
        when (nivelNumber) {

            // NIVEL 1 — INICIADO
            1 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — TELEVISOR"
                    tvDesc.text = "El televisor muestra un mensaje extraño.\nNecesitas imprimirlo en pantalla."
                    tvQuestion.text = "¿Cómo se imprime 'Hola Mundo' en Kotlin?"
                    tvHint.text = "💡 La función para imprimir en Kotlin termina con 'ln'"
                    respuestaCompleta = "println(\"Hola Mundo\")" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — COMPUTADOR"
                    tvDesc.text = "El computador pide una clave.\nDebes declararla como variable."
                    tvQuestion.text = "¿Cómo se declara una variable\nllamada 'clave' con valor 1234 en Kotlin?"
                    tvHint.text = "💡 En Kotlin puedes usar 'val' para valores fijos"
                    respuestaCompleta = "val clave = 1234" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — PUERTA FINAL"
                    tvDesc.text = "Combina lo que aprendiste\npara abrir la puerta."
                    tvQuestion.text = "Declara una variable 'codigo' con valor\n'ESCAPE' e imprímela"
                    tvHint.text = "💡 Usa val para la variable y println() para imprimir"
                    respuestaCompleta = "println(\"ESCAPE\")" }
            }

            // NIVEL 2 — GUERRERO
            2 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — ARCHIVADOR"
                    tvDesc.text = "El archivador tiene un documento\ncon una condición secreta."
                    tvQuestion.text = "¿Cómo se escribe un if en Kotlin\nque imprima 'acceso' si x es mayor a 5?"
                    tvHint.text = "💡 En Kotlin el if usa paréntesis y llaves { }"
                    respuestaCompleta = "if (x > 5) { println(\"acceso\") }" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — IMPRESORA"
                    tvDesc.text = "La impresora necesita una función\npara imprimir la clave."
                    tvQuestion.text = "¿Cómo se declara una función\nllamada 'imprimir' en Kotlin?"
                    tvHint.text = "💡 En Kotlin las funciones empiezan con la palabra 'fun'"
                    respuestaCompleta = "fun imprimir() { }" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — PUERTA FINAL"
                    tvDesc.text = "La puerta necesita un bucle\npara generar el código."
                    tvQuestion.text = "¿Cómo se escribe un bucle for\nque vaya del 1 al 5 en Kotlin?"
                    tvHint.text = "💡 En Kotlin usa 'in' con dos puntos (..) para el rango"
                    respuestaCompleta = "for (i in 1..5) { }" }
            }

            // NIVEL 3 — DEMENCIAL
            3 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — SERVIDOR"
                    tvDesc.text = "El servidor guarda datos en clases.\nNecesitas crear una para acceder."
                    tvQuestion.text = "¿Cómo se declara una clase\nllamada 'Usuario' en Kotlin?"
                    tvHint.text = "💡 Igual que en Java, usa 'class' seguido del nombre"
                    respuestaCompleta = "class Usuario { }" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — ROBOT"
                    tvDesc.text = "El robot maneja una lista de códigos.\nNecesitas crear una lista."
                    tvQuestion.text = "¿Cómo se crea una lista\ncon los elementos 1, 2, 3 en Kotlin?"
                    tvHint.text = "💡 Usa la función listOf() con los elementos entre paréntesis"
                    respuestaCompleta = "listOf(1, 2, 3)" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — PUERTA FINAL"
                    tvDesc.text = "La puerta evalúa múltiples condiciones.\nUsa la estructura correcta."
                    tvQuestion.text = "¿Cómo se escribe un 'when'\ncon los casos 1 y 2 en Kotlin?"
                    tvHint.text = "💡 'when' es el equivalente de switch, usa -> para cada caso"
                    respuestaCompleta = "when(x) { 1 -> ... 2 -> ... }" }
            }

            // NIVEL 4 — HACKERMAN
            4 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — CONSTRUCTOR"
                    tvDesc.text = "El objeto necesita nacer\ncon valores iniciales."
                    tvQuestion.text = "¿Cómo se define un constructor\nprimario en la clase 'Robot' en Kotlin?"
                    tvHint.text = "💡 En Kotlin el constructor va entre paréntesis junto al nombre de la clase"
                    respuestaCompleta = "class Robot(val nombre: String)" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — DATA CLASS"
                    tvDesc.text = "Una clase especial que guarda datos."
                    tvQuestion.text = "¿Cómo se declara una data class\nllamada 'Persona' en Kotlin?"
                    tvHint.text = "💡 Agrega la palabra 'data' antes de 'class'"
                    respuestaCompleta = "data class Persona(val nombre: String)" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — COMPANION"
                    tvDesc.text = "Métodos que pertenecen a la clase."
                    tvQuestion.text = "¿Cómo se declara un companion object\ndentro de una clase en Kotlin?"
                    tvHint.text = "💡 Son dos palabras: 'companion' y 'object' seguidas de llaves"
                    respuestaCompleta = "companion object { }" }
            }

            // NIVEL 5 — CORRUPTO
            5 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — HERENCIA"
                    tvDesc.text = "Un Guerrero es un Humano."
                    tvQuestion.text = "¿Cómo hereda Guerrero de Humano\nen Kotlin?"
                    tvHint.text = "💡 En Kotlin se usan dos puntos (:) en vez de 'extends'"
                    respuestaCompleta = "class Guerrero : Humano()" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — OPEN"
                    tvDesc.text = "En Kotlin las clases deben\npermitir ser heredadas."
                    tvQuestion.text = "¿Qué palabra se pone antes de 'class'\npara permitir herencia en Kotlin?"
                    tvHint.text = "💡 Por defecto las clases en Kotlin son cerradas — necesitas abrirlas"
                    respuestaCompleta = "open" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — OVERRIDE"
                    tvDesc.text = "Redefiniendo el comportamiento."
                    tvQuestion.text = "¿Qué palabra se usa para redefinir\nun método en Kotlin?"
                    tvHint.text = "💡 Es la misma palabra que en Java pero sin el símbolo @"
                    respuestaCompleta = "override" }
            }

            // NIVEL 6 — APOCALIPSIS
            6 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — INTERFACE"
                    tvDesc.text = "Un contrato que todas las\nclases deben cumplir."
                    tvQuestion.text = "¿Cómo se declara una interfaz\nllamada 'Volable' en Kotlin?"
                    tvHint.text = "💡 Usa 'interface' en lugar de 'class'"
                    respuestaCompleta = "interface Volable { }" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — LAMBDA"
                    tvDesc.text = "Funciones anónimas y compactas."
                    tvQuestion.text = "¿Cómo se escribe una lambda\nque suma 5 a un número 'n' en Kotlin?"
                    tvHint.text = "💡 Las lambdas van entre llaves { } con -> para separar parámetro y cuerpo"
                    respuestaCompleta = "{ n -> n + 5 }" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — EXTENSION"
                    tvDesc.text = "Agregando funciones a clases\nsin modificarlas."
                    tvQuestion.text = "¿Cómo se declara una función\nde extensión 'saludar' para String?"
                    tvHint.text = "💡 Usa fun NombreClase.nombreFuncion() — el tipo va antes del punto"
                    respuestaCompleta = "fun String.saludar() { }" }
            }

            // NIVEL 7 — DIOS DEL CÓDIGO
            7 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — CORRUTINAS"
                    tvDesc.text = "Tareas asíncronas sin bloquear\nel hilo principal."
                    tvQuestion.text = "¿Qué palabra marca una función\ncomo suspendible en Kotlin?"
                    tvHint.text = "💡 Va antes de 'fun' y tiene 7 letras"
                    respuestaCompleta = "suspend" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — FLOW"
                    tvDesc.text = "Flujo de datos reactivo."
                    tvQuestion.text = "¿Cómo se crea un Flow que emite\nlos números 1, 2, 3 en Kotlin?"
                    tvHint.text = "💡 Usa flow { } con emit() para cada valor que quieras emitir"
                    respuestaCompleta = "flow { emit(1); emit(2); emit(3) }" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — FINAL BOSS"
                    tvDesc.text = "El último reto del Dios del Código."
                    tvQuestion.text = "¿Cómo se obtiene el tamaño\nde una lista en Kotlin?"
                    tvHint.text = "💡 Es una propiedad de 4 letras que devuelve el número de elementos"
                    respuestaCompleta = "lista.size" }
            }
        }
    }

    private fun verificarRespuesta(respuesta: String): Boolean {
        return when (nivelNumber) {
            1 -> when (sublevelNumber) {
                1 -> respuesta.contains("println") && respuesta.contains("hola mundo")
                2 -> (respuesta.contains("val") || respuesta.contains("var")) && respuesta.contains("clave") && respuesta.contains("1234")
                3 -> respuesta.contains("println") && (respuesta.contains("codigo") || respuesta.contains("escape"))
                else -> false
            }
            2 -> when (sublevelNumber) {
                1 -> respuesta.contains("if") && respuesta.contains("5") && respuesta.contains("acceso")
                2 -> respuesta.contains("fun") && respuesta.contains("imprimir")
                3 -> respuesta.contains("for") && respuesta.contains("1") && respuesta.contains("5")
                else -> false
            }
            3 -> when (sublevelNumber) {
                1 -> respuesta.contains("class") && respuesta.contains("usuario")
                2 -> respuesta.contains("listof") && respuesta.contains("1") && respuesta.contains("2") && respuesta.contains("3")
                3 -> respuesta.contains("when") && respuesta.contains("1") && respuesta.contains("2")
                else -> false
            }
            4 -> when (sublevelNumber) {
                1 -> respuesta.contains("class") && respuesta.contains("robot")
                2 -> respuesta.contains("data") && respuesta.contains("class") && respuesta.contains("persona")
                3 -> respuesta.contains("companion") && respuesta.contains("object")
                else -> false
            }
            5 -> when (sublevelNumber) {
                1 -> respuesta.contains("guerrero") && respuesta.contains("humano")
                2 -> respuesta.contains("open")
                3 -> respuesta.contains("override")
                else -> false
            }
            6 -> when (sublevelNumber) {
                1 -> respuesta.contains("interface") && respuesta.contains("volable")
                2 -> respuesta.contains("->") && respuesta.contains("+") && respuesta.contains("5")
                3 -> respuesta.contains("fun") && respuesta.contains("string") && respuesta.contains("saludar")
                else -> false
            }
            7 -> when (sublevelNumber) {
                1 -> respuesta.contains("suspend")
                2 -> respuesta.contains("flow") && respuesta.contains("emit")
                3 -> respuesta.contains("size")
                else -> false
            }
            else -> false
        }
    }

    override fun onResume() { super.onResume(); MusicManager.iniciar(this) }
    override fun onPause() { super.onPause(); MusicManager.pausar() }
    override fun onDestroy() { super.onDestroy(); timer?.cancel(); playerExito?.release(); playerError?.release() }
}