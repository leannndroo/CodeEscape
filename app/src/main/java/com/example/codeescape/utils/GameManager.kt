package com.example.codeescape.utils

import android.content.Context

/**
 * GameManager — Maneja puntos, vidas y estadísticas globalmente
 */
object GameManager {

    // ── Vidas ──────────────────────────────────────────────
    const val MAX_VIDAS = 3
    var vidasActuales = MAX_VIDAS

    // ── Puntos ─────────────────────────────────────────────
    var puntosTotal = 0
    const val PUNTOS_CORRECTO = 100
    const val PUNTOS_RAPIDO = 50      // bonus por responder rápido
    const val PUNTOS_PISTA = -25      // penalización por usar pista

    // ── Estadísticas ───────────────────────────────────────
    var respuestasCorrectas = 0
    var respuestasIncorrectas = 0
    var tiempoTotalSegundos = 0L
    var nivelesCompletados = 0

    // ── Pistas ─────────────────────────────────────────────
    var pistasUsadas = 0

    /**
     * Sumar puntos al responder correctamente
     * Si respondió en menos de 10 segundos da bonus
     */
    fun respuestaCorrecta(tiempoSegundos: Long) {
        puntosTotal += PUNTOS_CORRECTO
        respuestasCorrectas++
        if (tiempoSegundos < 10) {
            puntosTotal += PUNTOS_RAPIDO
        }
    }

    /**
     * Restar vida al responder incorrectamente
     * Retorna true si aún quedan vidas
     */
    fun respuestaIncorrecta(): Boolean {
        respuestasIncorrectas++
        vidasActuales--
        return vidasActuales > 0
    }

    /**
     * Usar una pista — resta puntos
     */
    fun usarPista() {
        pistasUsadas++
        puntosTotal = maxOf(0, puntosTotal + PUNTOS_PISTA)
    }

    /**
     * Completar un nivel — suma puntos bonus
     */
    fun nivelCompletado() {
        nivelesCompletados++
        puntosTotal += 500
    }

    /**
     * Reiniciar vidas al empezar un nivel nuevo
     */
    fun reiniciarVidas() {
        vidasActuales = MAX_VIDAS
    }

    /**
     * Guardar estadísticas en SharedPreferences
     */
    fun guardar(context: Context) {
        val prefs = context.getSharedPreferences("CodeEscape", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("puntos_total", puntosTotal)
            putInt("respuestas_correctas", respuestasCorrectas)
            putInt("respuestas_incorrectas", respuestasIncorrectas)
            putInt("niveles_completados", nivelesCompletados)
            putInt("pistas_usadas", pistasUsadas)
            // Guardar mejor puntaje
            val mejorPuntaje = prefs.getInt("mejor_puntaje", 0)
            if (puntosTotal > mejorPuntaje) {
                putInt("mejor_puntaje", puntosTotal)
            }
            apply()
        }
    }

    /**
     * Cargar estadísticas guardadas
     */
    fun cargar(context: Context) {
        val prefs = context.getSharedPreferences("CodeEscape", Context.MODE_PRIVATE)
        puntosTotal = prefs.getInt("puntos_total", 0)
        respuestasCorrectas = prefs.getInt("respuestas_correctas", 0)
        respuestasIncorrectas = prefs.getInt("respuestas_incorrectas", 0)
        nivelesCompletados = prefs.getInt("niveles_completados", 0)
        pistasUsadas = prefs.getInt("pistas_usadas", 0)
    }

    /**
     * Resetear todo para jugar de nuevo
     */
    fun resetear() {
        vidasActuales = MAX_VIDAS
        puntosTotal = 0
        respuestasCorrectas = 0
        respuestasIncorrectas = 0
        tiempoTotalSegundos = 0L
        nivelesCompletados = 0
        pistasUsadas = 0
    }

    /**
     * Obtener rango según puntaje
     */
    fun obtenerRango(): String {
        return when {
            puntosTotal >= 10000 -> "🏆 MASTER CODER"
            puntosTotal >= 7000  -> "⭐ EXPERTO"
            puntosTotal >= 4000  -> "💻 PROGRAMADOR"
            puntosTotal >= 2000  -> "📚 ESTUDIANTE"
            else                 -> "🔰 PRINCIPIANTE"
        }
    }
}