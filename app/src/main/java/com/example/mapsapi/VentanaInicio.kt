package com.example.mapsapi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@Composable
fun VentanaInicio(navController: NavController,modifier: Modifier) {
    MyLog.d("Abrir ventana")

    Column(modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mapa1 Marcadores", fontWeight = FontWeight.Bold)
        Text("- Podemos ver un mapa con un icono personalizado " +
                "- Tiene dos marcadores, uno con animacion (FloatingMarker) y el otro sin ella" +
                "- Tiene el movimiento del mapa bloqueado para que solo sea algo visual" +
                "- Uno de los marcadores tiene un onClick, así puede actuar cómo un BOTON")
        Button({navController.navigate("mapa1")}) {Text("mapa1") }

        Text("Mapa2 Mapa con ubicacion", fontWeight = FontWeight.Bold)
        Text("- Comprueba si la app tiene permiso de ubicacion" +
                "- Si no hay permiso lo solicita" +
                "- Si consigue permiso, carga la latitud y longitud en una variable" +
                "- Tiene un boton que desplaza la camara a la ubicacion del usuario")
        Button({navController.navigate("mapa2")}) {Text("mapa2")}

        Text("Mapa3 Rutas y marcadores", fontWeight = FontWeight.Bold)
        Text("- Tiene varios marcadores y los conecta mediante rutas")
        Button({navController.navigate("mapa3")}) {Text("mapa3")}

        Text("Mapa4, usa una lista de localizaciones para definir la ubicacion de los marcadores")
        Button({navController.navigate("osmmap")}) {Text("OsmMap")}
        MyLog.d("Ventana inicial cargada")
    }
}