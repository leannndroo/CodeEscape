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

class SubLevelJavaActivity : AppCompatActivity() {

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

        btnBack.setOnClickListener { timer?.cancel(); setResult(RESULT_CANCELED); finish(); AnimManager.volver(this) }
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
            1 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — CONSOLA"; tvDesc.text = "La consola necesita mostrar\nun mensaje en Java."
                    tvQuestion.text = "¿Cómo se imprime 'Hola Mundo'\nen Java?"
                    tvHint.text = "💡 Usa System seguido de out y println()"
                    respuestaCompleta = "System.out.println(\"Hola Mundo\");" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — VARIABLE"; tvDesc.text = "El sistema necesita guardar\nuna variable en Java."
                    tvQuestion.text = "¿Cómo se declara una variable\nString llamada 'nombre' con valor 'Ana'?"
                    tvHint.text = "💡 En Java debes escribir el tipo de dato antes del nombre"
                    respuestaCompleta = "String nombre = \"Ana\";" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — PUERTA"; tvDesc.text = "Combina variable e impresión\npara abrir la puerta."
                    tvQuestion.text = "Declara un int llamado 'clave'\ncon valor 1234 e imprímelo"
                    tvHint.text = "💡 'int' es el tipo para números enteros en Java"
                    respuestaCompleta = "int clave = 1234; System.out.println(clave);" }
            }
            2 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — CONDICIÓN"; tvDesc.text = "El sistema evalúa condiciones\ncon if en Java."
                    tvQuestion.text = "¿Cómo se escribe un if en Java\nque imprima 'acceso' si x > 5?"
                    tvHint.text = "💡 En Java el if usa paréntesis y llaves { }"
                    respuestaCompleta = "if(x > 5) { System.out.println(\"acceso\"); }" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — BUCLE"; tvDesc.text = "El robot repite tareas\ncon for en Java."
                    tvQuestion.text = "¿Cómo se escribe un for en Java\nque vaya del 1 al 5?"
                    tvHint.text = "💡 El for de Java tiene 3 partes: inicio; condición; incremento"
                    respuestaCompleta = "for(int i=1; i<=5; i++) { }" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — MÉTODO"; tvDesc.text = "La puerta necesita un método\npara abrirse en Java."
                    tvQuestion.text = "¿Cómo se declara un método\nllamado 'abrir' en Java?"
                    tvHint.text = "💡 Usa 'void' cuando el método no devuelve nada"
                    respuestaCompleta = "void abrir() { }" }
            }
            3 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — CLASE"; tvDesc.text = "El servidor usa clases\npara organizar el código."
                    tvQuestion.text = "¿Cómo se declara una clase\nllamada 'Usuario' en Java?"
                    tvHint.text = "💡 Usa 'public class' seguido del nombre"
                    respuestaCompleta = "public class Usuario { }" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — ARREGLO"; tvDesc.text = "El sistema guarda múltiples\nvalores en arreglos."
                    tvQuestion.text = "¿Cómo se declara un arreglo\nde int con valores 1, 2, 3 en Java?"
                    tvHint.text = "💡 Usa int[] seguido del nombre y los valores entre llaves"
                    respuestaCompleta = "int[] nums = {1, 2, 3};" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — PUERTA FINAL"; tvDesc.text = "La puerta maneja excepciones\npara el acceso final."
                    tvQuestion.text = "¿Cómo se escribe un try-catch\nen Java para manejar errores?"
                    tvHint.text = "💡 Son dos bloques: try { } y catch(Exception e) { }"
                    respuestaCompleta = "try { } catch(Exception e) { }" }
            }
            4 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — CONSTRUCTOR"; tvDesc.text = "El objeto necesita nacer\ncon valores iniciales."
                    tvQuestion.text = "¿Cómo se define un constructor\npara la clase 'Robot'?"
                    tvHint.text = "💡 El constructor tiene el mismo nombre que la clase"
                    respuestaCompleta = "public Robot() { }" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — THIS"; tvDesc.text = "El robot debe referenciarse\na sí mismo."
                    tvQuestion.text = "¿Qué palabra clave se usa para\nreferirse al objeto actual?"
                    tvHint.text = "💡 Es una palabra de 4 letras que empieza con 't'"
                    respuestaCompleta = "this" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — SOBRECARGA"; tvDesc.text = "Un método, múltiples formas."
                    tvQuestion.text = "Define dos métodos 'sumar',\nuno con (int a) y otro con (int a, int b)"
                    tvHint.text = "💡 Mismo nombre, distintos parámetros — eso es sobrecarga"
                    respuestaCompleta = "void sumar(int a) { } void sumar(int a, int b) { }" }
            }
            5 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — HERENCIA"; tvDesc.text = "Un Guerrero es un Humano."
                    tvQuestion.text = "¿Cómo hereda Guerrero de Humano\nen Java?"
                    tvHint.text = "💡 En Java se usa la palabra 'extends'"
                    respuestaCompleta = "class Guerrero extends Humano { }" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — SUPER"; tvDesc.text = "Llamando al padre desde el hijo."
                    tvQuestion.text = "¿Cómo llamas al constructor de la\nclase padre?"
                    tvHint.text = "💡 Es una palabra que significa 'superior' seguida de ()"
                    respuestaCompleta = "super();" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — OVERRIDE"; tvDesc.text = "Redefiniendo el comportamiento."
                    tvQuestion.text = "¿Qué anotación se usa para indicar\nque un método es redefinido?"
                    tvHint.text = "💡 Empieza con @ y significa 'reemplazar'"
                    respuestaCompleta = "@Override" }
            }
            6 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — ABSTRACTO"; tvDesc.text = "Una idea sin forma todavía."
                    tvQuestion.text = "¿Cómo declaras una clase abstracta\nllamada 'Forma'?"
                    tvHint.text = "💡 Agrega una palabra antes de 'class' que significa 'abstracto'"
                    respuestaCompleta = "abstract class Forma { }" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — INTERFAZ"; tvDesc.text = "Un contrato que se debe cumplir."
                    tvQuestion.text = "¿Cómo declaras una interfaz\nllamada 'Volable'?"
                    tvHint.text = "💡 Cambia la palabra 'class' por otra que empieza con 'i'"
                    respuestaCompleta = "interface Volable { }" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — IMPLEMENTS"; tvDesc.text = "Firmando el contrato."
                    tvQuestion.text = "¿Cómo indica una clase 'Avion'\nque cumple la interfaz 'Volable'?"
                    tvHint.text = "💡 Usa la palabra 'implements' después del nombre de la clase"
                    respuestaCompleta = "class Avion implements Volable { }" }
            }
            7 -> when (sublevelNumber) {
                1 -> { tvTitle.text = "SUBNIVEL 1 — ARRAYLIST"; tvDesc.text = "Una lista que crece sin fin."
                    tvQuestion.text = "¿Cómo creas un ArrayList de\nStrings llamado 'lista'?"
                    tvHint.text = "💡 Usa ArrayList<String> y luego new ArrayList<>()"
                    respuestaCompleta = "ArrayList<String> lista = new ArrayList<>();" }
                2 -> { tvTitle.text = "SUBNIVEL 2 — ADD"; tvDesc.text = "Agregando elementos al arsenal."
                    tvQuestion.text = "¿Cómo agregas 'item1' a la\nvariable 'lista'?"
                    tvHint.text = "💡 Usa el método .add() sobre la lista"
                    respuestaCompleta = "lista.add(\"item1\");" }
                3 -> { tvTitle.text = "SUBNIVEL 3 — FINAL BOSS"; tvDesc.text = "El último reto de Java."
                    tvQuestion.text = "¿Cómo obtienes el tamaño de\nuna lista en Java?"
                    tvHint.text = "💡 Hay un método de 4 letras que devuelve el tamaño"
                    respuestaCompleta = "lista.size()" }
            }
        }
    }

    private fun verificarRespuesta(respuesta: String): Boolean {
        return when (nivelNumber) {
            1 -> when (sublevelNumber) {
                1 -> respuesta.contains("system.out.println") && respuesta.contains("hola mundo")
                2 -> respuesta.contains("string") && respuesta.contains("nombre") && respuesta.contains("ana")
                3 -> respuesta.contains("int") && respuesta.contains("clave") && respuesta.contains("1234")
                else -> false
            }
            2 -> when (sublevelNumber) {
                1 -> respuesta.contains("if") && respuesta.contains("5") && respuesta.contains("acceso")
                2 -> respuesta.contains("for") && respuesta.contains("i") && respuesta.contains("5")
                3 -> respuesta.contains("void") && respuesta.contains("abrir")
                else -> false
            }
            3 -> when (sublevelNumber) {
                1 -> respuesta.contains("class") && respuesta.contains("usuario")
                2 -> (respuesta.contains("int[]") || respuesta.contains("int []")) && respuesta.contains("1") && respuesta.contains("2") && respuesta.contains("3")
                3 -> respuesta.contains("try") && respuesta.contains("catch")
                else -> false
            }
            4 -> when (sublevelNumber) {
                1 -> respuesta.contains("robot") && respuesta.contains("()")
                2 -> respuesta.contains("this")
                3 -> respuesta.contains("sumar") && respuesta.contains("int a") && respuesta.contains("int b")
                else -> false
            }
            5 -> when (sublevelNumber) {
                1 -> respuesta.contains("extends") && respuesta.contains("guerrero") && respuesta.contains("humano")
                2 -> respuesta.contains("super")
                3 -> respuesta.contains("@override")
                else -> false
            }
            6 -> when (sublevelNumber) {
                1 -> respuesta.contains("abstract") && respuesta.contains("class") && respuesta.contains("forma")
                2 -> respuesta.contains("interface") && respuesta.contains("volable")
                3 -> respuesta.contains("implements") && respuesta.contains("avion") && respuesta.contains("volable")
                else -> false
            }
            7 -> when (sublevelNumber) {
                1 -> respuesta.contains("arraylist") && respuesta.contains("string") && respuesta.contains("new")
                2 -> respuesta.contains("lista.add")
                3 -> respuesta.contains("lista.size")
                else -> false
            }
            else -> false
        }
    }

    override fun onResume() { super.onResume(); MusicManager.iniciar(this) }
    override fun onPause() { super.onPause(); MusicManager.pausar() }
    override fun onDestroy() { super.onDestroy(); timer?.cancel(); playerExito?.release(); playerError?.release() }
}