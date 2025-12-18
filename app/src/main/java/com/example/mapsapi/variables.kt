package com.example.mapsapi

import com.example.mapsapi.VentanaMapa.MapPoint
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

const val barakaLat = 43.298916
const val barakaLong = -2.991845
const val defaultZoom = 17f

const val REQUEST_LOCATION_PERMISSION_CODE = 1001
const val REQUEST_CAMERA_PERMISSION_CODE = 1002
const val REQUEST_STORAGE_PERMISSION_CODE = 1003

// Settings predeterminadas para que el mapa tenga la ventana bloqueda
val uiSettingsBloqueado = MapUiSettings(
    scrollGesturesEnabled = false,   // Bloquea mover el mapa
    zoomGesturesEnabled = false,     // Bloquea zoom con gestos
    tiltGesturesEnabled = false,
    scrollGesturesEnabledDuringRotateOrZoom  = false
)

val uiSettingsDesbloqueado = MapUiSettings(
    scrollGesturesEnabled = true,
    zoomGesturesEnabled = true,
    tiltGesturesEnabled = true,
    scrollGesturesEnabledDuringRotateOrZoom  = true
)

// Por defecto no queremos que funcione con temas de ubicacion
val properties = MapProperties(
    isMyLocationEnabled = false
)

val mapPoints = listOf(
    MapPoint(1, 43.2570836,-2.9818232, "Ermita Santa Águeda"),
    MapPoint(2, 43.2957167,-2.9970082, "Iglesia de San Vicente"),
    MapPoint(3, 43.296896,-2.9898175, "Mercado de Abastos"),
    MapPoint(4, 43.3024855,-2.9861000, "Edificio Ilgner"),
    MapPoint(5, 43.2945499,-2.9774343, "Cargadero de minas"),
    MapPoint(6, 43.3004789,-2.9875229, "Ferrocarril"),
    MapPoint(7, 43.284434,-2.9808409, "Palacio Munoa")
)