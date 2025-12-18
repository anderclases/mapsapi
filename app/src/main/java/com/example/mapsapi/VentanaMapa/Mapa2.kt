package com.example.mapsapi.VentanaMapa

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.mapsapi.MyLog
import com.example.mapsapi.REQUEST_LOCATION_PERMISSION_CODE
import com.example.mapsapi.barakaLat
import com.example.mapsapi.barakaLong
import com.example.mapsapi.defaultZoom
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch




@Composable
fun Mapa2(navController: NavController,fusedLocationClient: FusedLocationProviderClient,modifier: Modifier) {
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val scope = rememberCoroutineScope()

    // INI obtener ubicacion actual
    LaunchedEffect(Unit) { // Ejecuta este código una sola vez cuando la pantalla se crea
        MyLog.d("Abrir ventana")
        // Pedir permiso de ubicacion
        val activity = context as? Activity
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MyLog.d("Se ha visto que no hay permisos de ubicacion asi que se van a pedir")

            // Pedir permiso
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION_CODE
                )
            }
        }
        // Si permiso ubicacion concedido lo cargamos
        if (ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            MyLog.d("Tenemos la ubicacion la cargamos en el userLocation")
            // verifica si la app tiene permiso para acceder a la ubicación precisa del usuario
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    MyLog.d("No hay ubicacion, por defecto pone la ubicacion de Barakaldo")
                    userLocation = LatLng(it.latitude, it.longitude)
                }
            }
        }
    }
    // FIN obtener ubicacion actual

    // INI cargar camara ubicacion actual
    val cameraPositionState = rememberCameraPositionState {
        userLocation?.let { // hace como un if else
            // Si la location != null
            MyLog.d("El userLocation tiene ubicacion, manda la camara a la ubicacion")
            position = CameraPosition.fromLatLngZoom(it, defaultZoom)
        } ?: run {
            // Si location == null
            MyLog.d("El userLocation no tiene ubicacion, manda la camara a Barakaldo")
            position = CameraPosition.fromLatLngZoom(LatLng(barakaLat, barakaLong), defaultZoom)
        }
    }
    // FIN cargar camara ubicacion actual

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = userLocation != null)
            ) {
                // Crea un marcador en la ubicacion actual
                userLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Mi ubicación"
                    )
                }
            }
            // Botón flotante para centrar la cámara en la ubicación
            CenterLocationButton(
                userLocation = userLocation,
                cameraPositionState = cameraPositionState,
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
    Text("Aqui acaba el mapa")
}

@Composable
fun CenterLocationButton(
    userLocation: LatLng?,
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            userLocation?.let {
                scope.launch {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(it, 15f)
                    )
                }
            }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Centrar en mi ubicación"
        )
    }
}
