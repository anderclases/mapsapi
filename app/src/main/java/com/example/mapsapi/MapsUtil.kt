package com.example.mapsapi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.app.ActivityCompat

class MapsUtil {
    companion object {
        /**
         * Comprueba si el usuario ha dado permiso de ubicacion a la app
         * Recomendable ejecutar antes de una transicion de ubicación
         */
        fun permisoUbicacion(context: Context): Boolean {
            var permiso:Boolean = false
            permiso = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            return permiso;
        }


        fun createNumberedMarker(
            number: Int
        ): Bitmap {

            val size = 96 // px
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            // Fondo circular
            val circlePaint = Paint().apply {
                color = 0xFFFFC93C.toInt()
                isAntiAlias = true
            }

            canvas.drawCircle(
                size / 2f,
                size / 2f,
                size / 2f,
                circlePaint
            )

            // Texto (número)
            val textPaint = Paint().apply {
                color = Color.BLACK
                textSize = 42f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
                typeface = Typeface.DEFAULT_BOLD
            }

            val textY = size / 2f - (textPaint.descent() + textPaint.ascent()) / 2
            canvas.drawText(
                number.toString(),
                size / 2f,
                textY,
                textPaint
            )

            return bitmap
        }
    }

}