package com.example.mapsapi

import android.util.Log

/**
 * Es una form global de llamar a un log, asi podemos estandarizar el menseje
 * Aseguramos que todos los logs tengan un [MyApp]
 * También informa desde que funcion se esta realizando el log
 */
object MyLog {

    private const val PREFIX = "[MyApp] "

    private fun caller(): String {
        val trace = Throwable().stackTrace

        // Buscar el primer método que no sea interno de Compose ni de MyLog
        val frame = trace.firstOrNull { element ->
            !element.className.startsWith("kotlin")
                    && !element.className.startsWith("android")
                    && !element.className.startsWith("java")
                    && !element.className.startsWith("androidx.compose")
                    && !element.className.contains("MyLog")
        }

        return frame?.methodName ?: "Unknown"
    }

    fun d(message: String) {
        Log.d(caller(), PREFIX + message)
    }

    fun e(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            // Log.e acepta un Throwable como tercer parámetro
            Log.e(caller(), PREFIX + message, throwable)
        } else {
            Log.e(caller(), PREFIX + message)
        }
    }
}