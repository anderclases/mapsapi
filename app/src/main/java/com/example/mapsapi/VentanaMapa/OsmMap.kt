package com.example.mapsapi.VentanaMapa

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mapsapi.MapsUtil
import com.example.mapsapi.MyLog
import com.example.mapsapi.mapPoints
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.config.Configuration

@Composable
fun OsmMap(){
    val context = LocalContext.current

    // Configurar el User Agent (Esto soluciona el error)
    Configuration.getInstance().userAgentValue = context.packageName
    LaunchedEffect(Unit) {
        MyLog.d("Iniciando configuración de OSM con UserAgent: ${context.packageName}")
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            MapView(ctx).apply {
                Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", 0))

                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                controller.setZoom(16.0)
                controller.setCenter(
                    GeoPoint(43.30, -2.99)
                )

                mapPoints.forEach { point ->
                    try {
                        val marker = Marker(this)
                        marker.position = GeoPoint(point.lat, point.lng)
                        marker.title = point.name
                        marker.subDescription = "Punto ${point.id}"

                        // 👉 ICONO NUMERADO
                        marker.icon = BitmapDrawable(
                            resources,
                            MapsUtil.createNumberedMarker(point.id)
                        )

                        marker.setAnchor(
                            Marker.ANCHOR_CENTER,
                            Marker.ANCHOR_BOTTOM
                        )

                        overlays.add(marker)
                    } catch (e: Exception) {
                        MyLog.e("Error al crear marcador para el punto: ${point.id}", e)
                    }
                }
            }
        }
    )
}