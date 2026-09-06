package com.example.codeescape.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/**
 * ThemeManager — Maneja el modo oscuro y claro
 */
object ThemeManager {

    private const val PREF_THEME = "tema"
    private const val MODO_OSCURO = "oscuro"
    private const val MODO_CLARO = "claro"

    /**
     * Aplicar el tema guardado al iniciar la app
     */
    fun aplicarTema(context: Context) {
        val prefs = context.getSharedPreferences("CodeEscapeTheme", Context.MODE_PRIVATE)
        val tema = prefs.getString(PREF_THEME, MODO_OSCURO)
        if (tema == MODO_OSCURO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    /**
     * Cambiar entre modo oscuro y claro
     * Retorna el nuevo tema aplicado
     */
    fun cambiarTema(context: Context): String {
        val prefs = context.getSharedPreferences("CodeEscapeTheme", Context.MODE_PRIVATE)
        val temaActual = prefs.getString(PREF_THEME, MODO_OSCURO)

        return if (temaActual == MODO_OSCURO) {
            // Cambiar a claro
            prefs.edit().putString(PREF_THEME, MODO_CLARO).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            MODO_CLARO
        } else {
            // Cambiar a oscuro
            prefs.edit().putString(PREF_THEME, MODO_OSCURO).apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            MODO_OSCURO
        }
    }

    /**
     * Obtener el tema actual
     */
    fun obtenerTema(context: Context): String {
        val prefs = context.getSharedPreferences("CodeEscapeTheme", Context.MODE_PRIVATE)
        return prefs.getString(PREF_THEME, MODO_OSCURO) ?: MODO_OSCURO
    }

    /**
     * Verificar si está en modo oscuro
     */
    fun esModoOscuro(context: Context): Boolean {
        return obtenerTema(context) == MODO_OSCURO
    }
}