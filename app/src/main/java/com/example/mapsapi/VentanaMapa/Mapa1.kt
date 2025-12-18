package com.example.mapsapi.VentanaMapa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mapsapi.MyLog
import com.example.mapsapi.R
import com.example.mapsapi.barakaLat
import com.example.mapsapi.barakaLong
import com.example.mapsapi.defaultZoom
import com.example.mapsapi.properties
import com.example.mapsapi.uiSettingsBloqueado
import com.example.mapsapi.uiSettingsDesbloqueado
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Primer ejemplo para aprender funcionamiento de la api de google maps
 * La api key para funcionar está en Android manifest es el value que tenemos en meta-data
 */

@Composable
fun Mapa1(navController: NavController,modifier: Modifier) {
    // INICIO PREPARACIÓN DATOS
    LaunchedEffect(Unit) {
        MyLog.d("Abrir ventana")
    }
    // La usamos dos veces una para agregar el marcador, la otra para mover la camara hasta esa ubicacion
    val barakaldoPosition : LatLng  = LatLng(barakaLat, barakaLong)
    val barakaldoPosition2 : LatLng = LatLng(43.299916, -2.991845)
    // rememberCameraPositionState - Crea y recuerda el estado de la cámara del mapa.
    val cameraPositionState = rememberCameraPositionState {
        // Establece la posicion de la camara en base a una ubicacion concreta
        position = CameraPosition.fromLatLngZoom(barakaldoPosition, defaultZoom)
    }
    val currentZoom = cameraPositionState.position.zoom
    var mapaBloqueado by remember { mutableStateOf(true) }

    // FIN PREPARACIÓN DATOS

    // INICIO CONTENIDO VENTANA
    // Box sirve para apilar elementos uno encima de otro
    Column(modifier = modifier.fillMaxSize()) {
        // VENTANA 70% de la pantalla
        // Esta box contiene el mapa, la he pintado de rojo, para ver donde está contenido el mapa
        Box(modifier = modifier.fillMaxWidth().weight(7f).background(Color.Red)) {
            // Crea un mapa de google maps(arg1: Ocupa el espacio posible, arg2: donde se centra la cámara)
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties, // variables.kt
                uiSettings = if (mapaBloqueado) uiSettingsBloqueado else uiSettingsDesbloqueado //variables.kt
            ) {
                // Ahora que hemos creado el mapa metemos todos los marcadores que queramos
                // Marcador 1
                Marker(
                    state = MarkerState(position = barakaldoPosition), // Ubicacion del marcador
                    title = "Ejemplo", // Titulo del marcador aparece cuando lo tocas
                    snippet = "Este es un marcador en Barakaldo", // aparece debajo del titulo
                    // Hay también marcadores por defecto de otros colores https://developers.google.com/maps/documentation/android-sdk/reference/com/google/android/libraries/maps/model/BitmapDescriptorFactory?hl=es-419
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.marcador_mapa_64px) ,
                    onClick = {
                        MyLog.d("marcador seleccionado")
                        // Aqui podemos meter codigo cómo si fuera un button
                        true  // al finalizar tiene que devolver true
                    }
                )
                // Marcador 2
                FloatingMarker(barakaldoPosition2)
            }
        }
        // VENTANA 30% de la pantalla
        Box(modifier = Modifier.fillMaxWidth().weight(2f).background(Color.Gray)) {

            Column {
                Button({navController.navigate("inicio")}) { Text("Volver a inicio")}

                InitialPositionButton(barakaldoPosition,cameraPositionState)
                Button({
                    if(mapaBloqueado) {mapaBloqueado = false} else {mapaBloqueado = true}
                }) {if(mapaBloqueado) {Text("Desbloquear")} else {Text("Bloquear")}}
                Text("Zoom: $currentZoom" )
            }
        }
        Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Blue)) {
            Text("Abajo")
        }
    }
    // FIN CONTENIDO VENTANA
}


/**
 * Este es un elemento composable con el código para generar un marcador con animacion
 */
@Composable
fun FloatingMarker(position: LatLng) {
    val markerState = rememberMarkerState(position = position)

    // Guardamos el desplazamiento vertical
    var direction by remember { mutableStateOf(1) } // 1 = subir, -1 = bajar
    val delta = 0.00005  // movimiento pequeño en latitud (~5m)

    Marker(
        state = markerState,
        title = "Marcador flotante",
        icon = BitmapDescriptorFactory.fromResource(R.drawable.marcador_mapa_64px)
    )

    // https://developer.android.com/develop/ui/compose/side-effects?hl=es-419
    // Inicia una corrutina en la que se ejecuta el bloque de código que contiene
    LaunchedEffect(Unit) {
        while (true) {
            // Cambiamos la latitud ligeramente
            val newLat = markerState.position.latitude + direction * delta
            markerState.position = LatLng(newLat, markerState.position.longitude)

            // Cambiamos dirección si llegamos a un límite
            if (newLat > position.latitude + delta || newLat < position.latitude - delta) {
                direction *= -1
            }

            delay(100) // X ms por frame, ajusta velocidad
        }
    }
}

@Composable
fun InitialPositionButton(position: LatLng,cameraPositionState: CameraPositionState) {
    // Botón para volver a la posición inicial
    Button(onClick = {
        // Usamos una corrutina para animar la cámara
        CoroutineScope(Dispatchers.Main).launch {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(position, defaultZoom)
            )
        }
    }) {
        Text("Volver a la posición inicial")
    }
}


@Preview(showBackground = true)
@Composable
fun Mapa1Preview() {
    Mapa1(
        navController = rememberNavController(),
        modifier = Modifier
    )
}