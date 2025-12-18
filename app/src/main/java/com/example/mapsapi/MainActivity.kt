package com.example.mapsapi

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mapsapi.ui.theme.MapsapiTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapi.VentanaMapa.Mapa1
import com.example.mapsapi.VentanaMapa.Mapa2
import com.example.mapsapi.VentanaMapa.Mapa3
import com.example.mapsapi.VentanaMapa.OsmMap

/**
 * EXPLICACIONES GENERALES PARA QUE LA API FUNCIONE
 * Incluir esto en build.gradle.kts a nivel general
 *  id("com.google.gms.google-services") version "4.4.4" apply false
 *
 * A nivel de proyecto incluir un montón de dependencias para que funcione maps
 */
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapsapiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    GestorVentanas(modifier = Modifier.padding(innerPadding),fusedLocationClient)
                }
            }
        }
    }
}

@Composable
fun GestorVentanas(modifier: Modifier,fusedLocationClient: FusedLocationProviderClient) {
    MyLog.d("Programa iniciado")
    val context = LocalContext.current
    // Esto solo es para comprobar que estamos leyendo bien la api key de local.properties
    val key : String = getMapsApiKey(context)
    MyLog.d("La key obtenida es " + key)
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") { VentanaInicio(navController, modifier) }
        composable("mapa1") { Mapa1(navController, modifier) }
        composable("mapa2") { Mapa2(navController, fusedLocationClient, modifier) }
        composable("mapa3") { Mapa3(navController, modifier) }
        composable("osmmap") { OsmMap() }
    }
}


fun getMapsApiKey(context: Context): String {
    val resultado : String?
    try {
        val appInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        val bundle = appInfo.metaData
        resultado =  bundle.getString("com.google.android.geo.API_KEY")
    } catch (e: Exception) {
        return "Error"
    }
    if(resultado==null) {
        return "Error"
    } else {
        return resultado
    }
}



