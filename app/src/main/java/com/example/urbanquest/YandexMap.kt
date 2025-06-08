package com.example.urbanquest

import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.urbanquest.domain.model.ItemFromDB
import com.example.urbanquest.ui.components.TagItem
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.ui.viewmodel.UserViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.delay

@Composable
fun YandexMap(
    navController: NavHostController,
    userViewModel: UserViewModel,
    itemViewModel: ItemFromDBViewModel = viewModel(),
    lat: Double? = null,
    lon: Double? = null,
    showSelectedOnly: Boolean = false
) {
    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val onNavigateToPlaceItem = remember(navController, itemViewModel) {
        { place: ItemFromDB ->
            itemViewModel.selectPlace(place)
            navController.navigate("placeItem")
        }
    }

    val onClearSelectionsAndGoBack = remember(navController, itemViewModel) {
        {
            itemViewModel.clearSelections()
            navController.popBackStack()
            Unit
        }
    }

    var mapView by remember { mutableStateOf<MapView?>(null) }
    var mapObjects by remember { mutableStateOf<MapObjectCollection?>(null) }
    var locationManager by remember { mutableStateOf<LocationManager?>(null) }
    var userLocationPlacemark by remember { mutableStateOf<PlacemarkMapObject?>(null) }
    var currentUserLocation by remember { mutableStateOf<Point?>(null) }
    var currentUserBearing by remember { mutableStateOf<Float?>(null) }
    var isLocationLoading by remember { mutableStateOf(false) }
    var selectedPlace by remember { mutableStateOf<ItemFromDB?>(null) }
    var showInfoCard by remember { mutableStateOf(false) }
    var isDistanceInfoVisible by remember { mutableStateOf(false) }
    var distanceInfo by remember { mutableStateOf("") }

    val buttonColor = remember { Color(0xFFC9EA63) }

    val selectedPlaceIds by itemViewModel.selectedForMap.collectAsState()

    var placesToShow by remember { mutableStateOf<List<ItemFromDB>>(emptyList()) }
    var placesLoaded by remember { mutableStateOf(false) }

    val locationListener = remember {
        object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                val userPoint = location.position
                val bearing = location.heading?.toFloat()
                val accuracy = location.accuracy?.toFloat()
                if (accuracy == null || accuracy <= 50f) {
                    currentUserLocation = userPoint
                    currentUserBearing = bearing
                    isLocationLoading = false

                    mapObjects?.let { mapObjectCollection ->
                        userLocationPlacemark = MapUtils.updateLocationIcon(
                            mapObjects = mapObjectCollection,
                            currentPlacemark = userLocationPlacemark,
                            location = userPoint,
                            bearing = bearing,
                            context = context
                        )
                    }
                } else {
                    if (isLocationLoading) {
                        currentUserLocation = userPoint
                        currentUserBearing = bearing
                        isLocationLoading = false

                        mapObjects?.let { mapObjectCollection ->
                            userLocationPlacemark = MapUtils.updateLocationIcon(
                                mapObjects = mapObjectCollection,
                                currentPlacemark = userLocationPlacemark,
                                location = userPoint,
                                bearing = bearing,
                                context = context
                            )
                        }
                    }
                }
            }

            override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
                when (locationStatus) {
                    LocationStatus.NOT_AVAILABLE -> {
                        isLocationLoading = false
                        Toast.makeText(context, "Геолокация недоступна", Toast.LENGTH_SHORT).show()
                    }
                    LocationStatus.AVAILABLE -> {
                        Log.d("YandexMap", "Location is available")
                    }
                    LocationStatus.RESET -> {
                        Log.d("YandexMap", "Location reset - requesting new location")
                        locationManager?.requestSingleUpdate(this)
                    }
                }
            }
        }
    }

    val updateDistanceInfo = remember {
        { text: String ->
            distanceInfo = text
            isDistanceInfoVisible = true
        }
    }

    val hideInfoCard = remember {
        {
            showInfoCard = false
            isDistanceInfoVisible = false
        }
    }

    LaunchedEffect(selectedPlaceIds, lat, lon, showSelectedOnly) {
        placesToShow = when {
            showSelectedOnly -> {
                val walkingPlaces = itemViewModel.getWalkingPlaces(forceRefresh = true)
                val foodPlaces = itemViewModel.getFoodPlaces(forceRefresh = true)
                delay(300)
                val allPlaces = walkingPlaces + foodPlaces
                allPlaces.filter { selectedPlaceIds.contains(it.id) }
            }
            lat != null && lon != null -> {
                val allPlaces = itemViewModel.getWalkingPlaces() + itemViewModel.getFoodPlaces()
                allPlaces.filter { place ->
                    val placeLatitude = place.geopoint_latitude.toDoubleOrNull() ?: 0.0
                    val placeLongitude = place.geopoint_longtitude.toDoubleOrNull() ?: 0.0
                    val distance = MapUtils.calculateDistance(placeLatitude, placeLongitude, lat, lon)
                    distance < 1.0
                }
            }
            else -> emptyList()
        }
        placesLoaded = true
    }

    val mapUpdateData = remember(placesToShow, placesLoaded) {
        if (placesLoaded && placesToShow.isNotEmpty()) {
            val validPlaces = placesToShow.filter {
                it.geopoint_latitude.toDoubleOrNull() != null &&
                        it.geopoint_longtitude.toDoubleOrNull() != null
            }

            if (validPlaces.isNotEmpty()) {
                val points = validPlaces.map { place ->
                    Point(
                        place.geopoint_latitude.toDoubleOrNull()!!,
                        place.geopoint_longtitude.toDoubleOrNull()!!
                    )
                }

                val bounds = MapUtils.calculateBounds(points)
                val center = bounds?.let {
                    Point(
                        (it.first.latitude + it.second.latitude) / 2,
                        (it.first.longitude + it.second.longitude) / 2
                    )
                }
                val zoom = bounds?.let { MapUtils.calculateZoomFromBounds(it) }

                Triple(validPlaces, center, zoom)
            } else null
        } else null
    }

    DisposableEffect(Unit) {
        onDispose {
            locationManager?.unsubscribe(locationListener)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MapKitFactory.initialize(context)
                val view = LayoutInflater.from(context).inflate(R.layout.yandex_map, null, false)
                val mapViewInstance = view.findViewById<MapView>(R.id.mapview)
                mapView = mapViewInstance

                val map = mapViewInstance.mapWindow.map
                mapObjects = map.mapObjects.addCollection()

                if (hasLocationPermission) {
                    locationManager = MapUtils.setupLocationManager(context, locationListener)
                }

                val initialPoint = Point(55.751225, 37.629540)
                map.move(CameraPosition(initialPoint, 12.0f, 0.0f, 0.0f))

                map.addInputListener(object : InputListener {
                    override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {
                        hideInfoCard()
                    }
                    override fun onMapLongTap(map: com.yandex.mapkit.map.Map, point: Point) {}
                })

                view
            },
            update = { view ->
                val mapViewInstance = mapView ?: return@AndroidView
                val map = mapViewInstance.mapWindow.map

                if (hasLocationPermission && locationManager == null) {
                    locationManager = MapUtils.setupLocationManager(context, locationListener)
                } else if (!hasLocationPermission && locationManager != null) {
                    locationManager?.unsubscribe(locationListener)
                    userLocationPlacemark?.let { placemark ->
                        placemark.parent?.remove(placemark)
                    }
                    userLocationPlacemark = null
                    currentUserLocation = null
                    currentUserBearing = null
                }

                if (placesLoaded) {
                    mapObjects?.clear()

                    currentUserLocation?.let { userPoint ->
                        if (hasLocationPermission) {
                            userLocationPlacemark = MapUtils.updateLocationIcon(
                                mapObjects = mapObjects!!,
                                currentPlacemark = null,
                                location = userPoint,
                                bearing = currentUserBearing,
                                context = context
                            )
                        }
                    }

                    if (mapUpdateData != null) {
                        val (validPlaces, center, zoom) = mapUpdateData
                        if (center != null && zoom != null) {
                            map.move(CameraPosition(center, zoom, 0.0f, 0.0f))
                        }
                        MapUtils.addMarkersAndRoute(
                            context,
                            validPlaces,
                            mapObjects!!,
                            { clickedPlace ->
                                selectedPlace = clickedPlace
                                showInfoCard = true
                            },
                            updateDistanceInfo,
                            currentUserLocation
                        )
                    } else if (lat != null && lon != null) {
                        val point = Point(lat, lon)
                        map.move(CameraPosition(point, 15.0f, 0.0f, 0.0f))
                        MapUtils.addSinglePlaceMarker(context, mapObjects!!, point)
                    }
                }

                MapKitFactory.getInstance().onStart()
                mapViewInstance.onStart()
            },
            onRelease = { view ->
                val mapViewInstance = mapView ?: return@AndroidView
                locationManager?.unsubscribe(locationListener)
                mapViewInstance.onStop()
                MapKitFactory.getInstance().onStop()
            }
        )

        if (hasLocationPermission) {
            FloatingActionButton(
                onClick = {
                    if (currentUserLocation != null) {
                        mapView?.mapWindow?.map?.move(
                            CameraPosition(
                                currentUserLocation!!,
                                18.0f,
                                0.0f,
                                0.0f
                            )
                        )
                        Log.d("YandexMap", "Moved to user location with high zoom: $currentUserLocation")
                    } else {
                        isLocationLoading = true
                        MapUtils.requestSingleLocationUpdate(
                            locationManager,
                            context,
                            onLocationReceived = { userPoint, bearing ->
                                currentUserLocation = userPoint
                                currentUserBearing = bearing
                                isLocationLoading = false

                                mapObjects?.let { mapObjectCollection ->
                                    userLocationPlacemark = MapUtils.updateLocationIcon(
                                        mapObjects = mapObjectCollection,
                                        currentPlacemark = userLocationPlacemark,
                                        location = userPoint,
                                        bearing = bearing,
                                        context = context
                                    )
                                }

                                mapView?.mapWindow?.map?.move(
                                    CameraPosition(userPoint, 18.0f, 0.0f, 0.0f)
                                )

                            },
                            onLocationError = {
                                isLocationLoading = false
                                Toast.makeText(context, "Не удается определить местоположение. Проверьте GPS.", Toast.LENGTH_LONG).show()
                            }
                        )

                        Toast.makeText(context, "Поиск местоположения...", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .size(56.dp),
                containerColor = buttonColor
            ) {
                if (isLocationLoading) {
                    androidx.compose.material3.CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.navigation),
                        contentDescription = "Мое местоположение",
                        tint = Color.Black
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showInfoCard && selectedPlace != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (hasLocationPermission) 140.dp else 70.dp)
        ) {
            selectedPlace?.let { place ->
                val context = LocalContext.current
                val isFavorite = remember { mutableStateOf(userViewModel.isFavourite(place.id.toString())) }

                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .widthIn(max = 300.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = place.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.favourite_icon),
                                contentDescription = "Избранное",
                                tint = if (isFavorite.value) Color.Red else MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier
                                    .size(26.dp)
                                    .padding(2.dp)
                                    .clickable {
                                        if (userViewModel.isAuthorized.value) {
                                            if (isFavorite.value) {
                                                userViewModel.removeFromFavourites(place.id.toString()) { success ->
                                                    if (success) {
                                                        isFavorite.value = false
                                                    }
                                                }
                                            } else {
                                                userViewModel.addToFavourites(place.id.toString()) { success ->
                                                    if (success) {
                                                        isFavorite.value = true
                                                    }
                                                }
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Войдите, чтобы добавить в избранное",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                                contentDescription = "Rating",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = place.rate.toString(),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(place.tags.values.toList()) { tagValue ->
                                TagItem(value = tagValue)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { onNavigateToPlaceItem(place) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = buttonColor
                            )
                        ) {
                            Text(
                                text = "Подробнее",
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isDistanceInfoVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.8f)
                )
            ) {
                Text(
                    text = distanceInfo,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = showInfoCard,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { showInfoCard = false },
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }

        if (showSelectedOnly) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Выбрано мест: ${placesToShow.size}",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Button(
                onClick = onClearSelectionsAndGoBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Сбросить выбор",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Сбросить маршрут", color = Color.Black)
            }
        }
    }
}