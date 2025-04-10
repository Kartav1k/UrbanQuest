package com.example.urbanquest

import android.graphics.PointF
import android.util.Log
import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


@Composable
fun YandexMap(navController: NavHostController, isAuthorization: Boolean, lat: Double? = null, lon: Double? = null) {
    val context = LocalContext.current
    var currentPlacemark by remember { mutableStateOf<PlacemarkMapObject?>(null) }

    AndroidView(
        factory = { context ->
            MapKitFactory.initialize(context)
            val view = LayoutInflater.from(context).inflate(R.layout.yandex_map, null, false)
            val mapView = view.findViewById<MapView>(R.id.mapview)

            val map = mapView.mapWindow.map
            val point = if (lat != null && lon != null) Point(lat, lon) else Point(55.751225, 37.629540)
            map.move(CameraPosition(point, 12.0f, 0.0f, 0.0f))

            val mapObjects = map.mapObjects.addCollection()

            // Создание слушателя событий на карте

            val inputListener = object : InputListener {
                override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {
                    currentPlacemark?.let { mapObjects.remove(it) }
                    val imageProvider = ImageProvider.fromResource(context, R.drawable.label_user)
                    currentPlacemark = mapObjects.addPlacemark(point).apply {
                        setIcon(imageProvider)
                        setIconStyle(IconStyle().apply {
                            anchor = PointF(0.35f, 0.78f)
                            scale = 0.6f
                            zIndex = 10.0f
                        })
                    }
                    
                    val placemarkTapListener = MapObjectTapListener { _, tapPoint ->
                        Log.d("YandexMap", "Tapped the point (${tapPoint.latitude}, ${tapPoint.longitude})")
                        true
                    }
                    currentPlacemark?.addTapListener(placemarkTapListener)
                }

                override fun onMapLongTap(map: com.yandex.mapkit.map.Map, point: Point) {

                }
            }
            map.addInputListener(inputListener)

            view
        },
        update = { view ->
            val mapView = view.findViewById<MapView>(R.id.mapview)
            MapKitFactory.getInstance().onStart()
            mapView.onStart()
        },
        onRelease = { view ->
            val mapView = view.findViewById<MapView>(R.id.mapview)
            mapView.onStop()
            MapKitFactory.getInstance().onStop()
        }
    )
}