package com.example.mapsapi.VentanaMapa


import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.mapsapi.MapsUtil
import com.example.mapsapi.MyLog
import com.example.mapsapi.mapPoints
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

data class MapPoint(
    val id: Int,
    val lat: Double,
    val lng: Double,
    val name: String
)

@Composable
fun Mapa3(navController: NavController,modifier: Modifier) {
    LaunchedEffect(Unit) {
        MyLog.d("Abrir ventana")
    }
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }

    var puntoA by remember { mutableStateOf<LatLng?>(null) }
    var puntoB by remember { mutableStateOf<LatLng?>(null) }

    var distancia by remember { mutableStateOf("") }
    // Datos hardcodeados de momento


    Column(modifier = modifier.fillMaxSize()) {
        // VENTANA 70% de la pantalla
        // Esta box contiene el mapa, la he pintado de rojo, para ver donde está contenido el mapa
        Box(modifier = Modifier.fillMaxWidth().weight(7f).background(Color.Red)) {
            cargarMapa()
        }
        Box(modifier = Modifier.fillMaxWidth().weight(3f)) {
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = origen,
                    onValueChange = { origen = it },
                    label = { Text("Origen (id)") }
                )

                OutlinedTextField(
                    value = destino,
                    onValueChange = { destino = it },
                    label = { Text("Destino (id)") }
                )

                Button(onClick = {
                    puntoA = getLatLng(origen)
                    puntoB = getLatLng(destino)
                    if (puntoA != null && puntoB != null) {
                        MyLog.d("Boton Calcular distancia")
                        distancia = calcularDistancia(puntoA!!, puntoB!!)
                    }
                }) {
                    Text("Calcular ruta")
                }
                Text(text = "Distancia: $distancia")
            }

        }
    }

}

@Composable
fun cargarMapa() {

    // Cámara centrada en la zona de los puntos
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(43.30, -2.99),
            14f
        )
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.SATELLITE,
            mapStyleOptions = MapStyleOptions(MapConfig.STYLE_JSON)
        )
    ) {
        ponerMapPointsPersonalizados()
    }
}

@Composable
fun ponerMapPointsPersonalizados() {
    mapPoints.forEach { point ->
        // 1. Convertimos el Bitmap a BitmapDescriptor usando remember
        val customIcon = remember(point.id) {
            val bitmap = MapsUtil.createNumberedMarker(point.id)
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        // 2. Lo pasamos al parámetro 'icon'
        Marker(
            state = MarkerState(
                position = LatLng(point.lat, point.lng)
            ),
            title = point.name,
            snippet = "Punto ${point.id}",
            icon = customIcon, // <--- Aquí aplicas tu bitmap
            onClick = {
                false // Retornar false permite que se siga viendo el snippet al tocar
            }
        )
    }
}

@Composable
fun ponerMapPoints() {
    mapPoints.forEach { point ->
        // Creamos un markeer por cada elemento de la lista
        Marker(
            // Se define ubicacion del marcador
            state = MarkerState(
                position = LatLng(point.lat, point.lng)
            ),
            // Titulo del marcador
            title = point.name,
            // Descripcion del marcador
            snippet = "Punto ${point.id}",
            // al clicar por ahora no hace nada especial
            onClick = {
                true
            }
        )
    }
}


fun getLatLng(text: String): LatLng? {
    return try {
        val parts = text.split(",")
        LatLng(mapPoints[text.toInt()].lat, mapPoints[text.toInt()].lng)
    } catch (e: Exception) {
        null
    }
}


fun calcularDistancia(a: LatLng, b: LatLng): String {
    val results = FloatArray(1)

    // Mostramos ambos datos de cada punto
    MyLog.d("Origen: Lat ${a.latitude}, Lng ${a.longitude}")
    MyLog.d("Destino: Lat ${b.latitude}, Lng ${b.longitude}")

    Location.distanceBetween(
        a.latitude, a.longitude,
        b.latitude, b.longitude,
        results
    )

    val metros = results[0]
    val km = metros / 1000

    // Traza final completa con todos los datos
    MyLog.d("Cálculo final: $metros m entre A(${a.latitude}, ${a.longitude}) y B(${b.latitude}, ${b.longitude})")

    return String.format("%.2f km", km)
}

object MapConfig {
    const val STYLE_JSON = """
    [
      {
        "featureType": "poi.business",
        "elementType": "all",
        "stylers": [ { "visibility": "off" } ]
      }
    ]
    """
}