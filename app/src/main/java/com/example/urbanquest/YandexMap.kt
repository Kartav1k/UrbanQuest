package com.example.urbanquest

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView


private lateinit var mapView: MapView
@Composable
fun YandexMap(navController: NavHostController, isAuthorization: Boolean){
    MapKitFactory.initialize(LocalContext.current)
    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.yandex_map, null, false)

            mapView = view.findViewById<MapView>(R.id.mapview)

            // do whatever you want...
            view // return the view
        },
        update = { view ->
            MapKitFactory.getInstance().onStart()
            mapView.onStart()
            
        }
    )
}