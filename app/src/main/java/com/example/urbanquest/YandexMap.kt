package com.example.urbanquest

import android.content.Context
import android.graphics.PointF
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.urbanquest.domain.model.ItemFromDB
import com.example.urbanquest.ui.components.TagItem
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.ui.viewmodel.UserViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
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
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var mapObjects by remember { mutableStateOf<MapObjectCollection?>(null) }
    var selectedPlace by remember { mutableStateOf<ItemFromDB?>(null) }
    var showInfoCard by remember { mutableStateOf(false) }

    var isDistanceInfoVisible by remember { mutableStateOf(false) }
    var distanceInfo by remember { mutableStateOf("") }

    val ButtonColor = Color(0xFFC9EA63)

    // Используем collectAsState для получения актуального списка выбранных ID
    val selectedPlaceIds by itemViewModel.selectedForMap.collectAsState()

    // Состояние для отслеживания загруженных мест
    var placesToShow by remember { mutableStateOf<List<ItemFromDB>>(emptyList()) }
    var placesLoaded by remember { mutableStateOf(false) }

    // Функция для обновления информации о расстоянии
    val updateDistanceInfo: (String) -> Unit = { text ->
        distanceInfo = text
        isDistanceInfoVisible = true
    }

    // Определяем, какие места нужно показать
    LaunchedEffect(selectedPlaceIds, lat, lon, showSelectedOnly) {
        if (showSelectedOnly) {
            // Принудительно загружаем кэши для уверенности, что данные будут доступны
            val walkingPlaces = itemViewModel.getWalkingPlaces(forceRefresh = true)
            val foodPlaces = itemViewModel.getFoodPlaces(forceRefresh = true)

            // Ждем некоторое время для завершения загрузки данных
            delay(300)

            // Получаем и фильтруем все места, выбранные для карты
            val allPlaces = walkingPlaces + foodPlaces
            val selectedPlaces = allPlaces.filter { selectedPlaceIds.contains(it.id) }

            Log.d("YandexMap", "Selected places IDs: $selectedPlaceIds")
            Log.d("YandexMap", "All places count: ${allPlaces.size}")
            Log.d("YandexMap", "Filtered places count: ${selectedPlaces.size}")
            Log.d("YandexMap", "Filtered place names: ${selectedPlaces.map { it.name }}")

            placesToShow = selectedPlaces
        } else if (lat != null && lon != null) {
            val allPlaces = itemViewModel.getWalkingPlaces() + itemViewModel.getFoodPlaces()
            val nearbyPlaces = allPlaces.filter { place ->
                val placeLatitude = place.geopoint_latitude.toDoubleOrNull() ?: 0.0
                val placeLongitude = place.geopoint_longtitude.toDoubleOrNull() ?: 0.0
                val distance = calculateDistance(placeLatitude, placeLongitude, lat, lon)
                distance < 0.001
            }
            placesToShow = nearbyPlaces
        } else {
            placesToShow = emptyList()
        }

        placesLoaded = true
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

                // Создаем коллекцию объектов
                mapObjects = map.mapObjects.addCollection()

                // По умолчанию центр Москвы
                var initialPoint = Point(55.751225, 37.629540)
                var zoom = 12.0f

                map.move(CameraPosition(initialPoint, zoom, 0.0f, 0.0f))

                // Обработчик нажатия на карту
                map.addInputListener(object : InputListener {
                    override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {
                        // Скрываем карточку с информацией
                        showInfoCard = false
                        isDistanceInfoVisible = false
                    }

                    override fun onMapLongTap(map: com.yandex.mapkit.map.Map, point: Point) {
                        // Можно добавить дополнительные действия при долгом нажатии
                    }
                })

                view
            },
            update = { view ->
                val mapViewInstance = mapView ?: return@AndroidView
                val map = mapViewInstance.mapWindow.map

                // Обновляем карту только когда места загружены
                if (placesLoaded) {
                    // Очищаем все объекты карты
                    mapObjects?.clear()

                    Log.d("YandexMap", "Updating map with ${placesToShow.size} places")

                    if (placesToShow.isNotEmpty()) {
                        // Добавляем метки и линии
                        if (showSelectedOnly || (lat != null && lon != null)) {
                            // Вычисляем центр и масштаб
                            val validPlaces = placesToShow.filter {
                                it.geopoint_latitude.toDoubleOrNull() != null &&
                                        it.geopoint_longtitude.toDoubleOrNull() != null
                            }

                            Log.d("YandexMap", "Valid places with coordinates: ${validPlaces.size}")

                            if (validPlaces.isNotEmpty()) {
                                val points = validPlaces.map { place ->
                                    Point(
                                        place.geopoint_latitude.toDoubleOrNull()!!,
                                        place.geopoint_longtitude.toDoubleOrNull()!!
                                    )
                                }

                                // Устанавливаем центр и масштаб
                                val bounds = calculateBounds(points)
                                if (bounds != null) {
                                    val center = Point(
                                        (bounds.first.latitude + bounds.second.latitude) / 2,
                                        (bounds.first.longitude + bounds.second.longitude) / 2
                                    )
                                    val zoom = calculateZoomFromBounds(bounds)

                                    Log.d("YandexMap", "Moving camera to center: $center, zoom: $zoom")
                                    map.move(CameraPosition(center, zoom, 0.0f, 0.0f))
                                }

                                // Добавляем метки и линии
                                addMarkersAndLinesNew(
                                    context,
                                    validPlaces,
                                    mapObjects!!,
                                    { clickedPlace ->
                                        selectedPlace = clickedPlace
                                        showInfoCard = true
                                    },
                                    updateDistanceInfo
                                )
                            } else if (lat != null && lon != null) {
                                // Если нет валидных мест, но есть координаты для центра
                                val point = Point(lat, lon)
                                map.move(CameraPosition(point, 15.0f, 0.0f, 0.0f))

                                // Добавляем метку пользователя
                                val imageProvider = ImageProvider.fromResource(context, R.drawable.label_user)
                                mapObjects!!.addPlacemark(point).apply {
                                    setIcon(imageProvider)
                                    setIconStyle(IconStyle().apply {
                                        anchor = PointF(0.5f, 1.0f)
                                        scale = 0.6f
                                        zIndex = 10.0f
                                    })
                                }
                            }
                        }
                    }
                }

                MapKitFactory.getInstance().onStart()
                mapViewInstance.onStart()
            },
            onRelease = { view ->
                val mapViewInstance = mapView ?: return@AndroidView
                mapViewInstance.onStop()
                MapKitFactory.getInstance().onStop()
            }
        )

        // Показываем информационную карточку о месте
        AnimatedVisibility(
            visible = showInfoCard && selectedPlace != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp)
        ) {
            selectedPlace?.let { place ->
                val context = LocalContext.current
                // State для отслеживания статуса избранного
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
                        // Заголовок и кнопка избранного в одной строке
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Название места
                            Text(
                                text = place.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            // Кнопка избранного
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

                        // Рейтинг
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

                        // Теги - показываем все теги в горизонтальном списке
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(place.tags.values.toList()) { tagValue ->
                                TagItem(value = tagValue)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Кнопка "Подробнее"
                        Button(
                            onClick = {
                                itemViewModel.selectPlace(place)
                                navController.navigate("placeItem")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonColor
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

        // Кнопка закрытия карточки
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

        // Счетчик выбранных мест
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

            // Кнопка "Сбросить маршрут"
            Button(
                onClick = {
                    itemViewModel.clearSelections()
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonColor
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

private fun addMarkersAndLinesNew(
    context: Context,
    places: List<ItemFromDB>,
    mapObjects: MapObjectCollection,
    onPlaceSelected: (ItemFromDB) -> Unit,
    onDistanceUpdated: (String) -> Unit
) {
    if (places.isEmpty()) {
        Log.d("YandexMap", "No places to add markers for")
        return
    }

    Log.d("YandexMap", "Adding markers for ${places.size} places")

    // Список точек для создания ломаной
    val points = mutableListOf<Point>()
    val placesForPoints = mutableListOf<ItemFromDB>()

    // Сначала собираем все валидные точки
    places.forEach { place ->
        val latitude = place.geopoint_latitude.toDoubleOrNull()
        val longitude = place.geopoint_longtitude.toDoubleOrNull()

        if (latitude != null && longitude != null) {
            val point = Point(latitude, longitude)
            points.add(point)
            placesForPoints.add(place)
        }
    }

    // Оптимизируем порядок точек с использованием улучшенного алгоритма
    val optimizedRoute = findOptimalRouteImproved(points, placesForPoints)
    val optimizedPoints = optimizedRoute.first
    val optimizedPlaces = optimizedRoute.second

    // Создаем полилинию оптимизированного маршрута
    if (optimizedPoints.size >= 2) {
        val polyline = Polyline(optimizedPoints)
        val polylineObject = mapObjects.addPolyline(polyline)
        polylineObject.setStrokeColor(Color(0xFF4CAF50).toArgb()) // Зеленый цвет
        polylineObject.strokeWidth = 5f // Ширина линии
        polylineObject.zIndex = 10f
    }

    // Теперь добавляем метки на оптимизированном маршруте
    for (i in optimizedPoints.indices) {
        val point = optimizedPoints[i]
        val place = optimizedPlaces[i]

        // Создаем простую синюю метку
        val placemark = mapObjects.addPlacemark(point)

        // Настраиваем внешний вид метки - просто синяя точка
        placemark.setIcon(ImageProvider.fromBitmap(createCircleBitmap(context, Color(0xFF2196F3).toArgb())))
        placemark.setIconStyle(IconStyle().apply {
            anchor = PointF(0.5f, 0.5f) // Центрируем точку
            scale = 1.0f
            zIndex = 15.0f
        })

        // Добавляем подпись под меткой
        placemark.setText(place.name, TextStyle().apply {
            placement = TextStyle.Placement.BOTTOM // Текст под меткой
            offset = 3f // Отступ от метки
            color = Color.Black.toArgb() // Черный цвет текста
            size = 12f // Размер шрифта
        })

        // Обработчик нажатия на метку
        placemark.addTapListener(MapObjectTapListener { _, _ ->
            onPlaceSelected(place)
            true
        })
    }
}

// Функция для расчета расстояния между координатами
private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val latDiff = lat1 - lat2
    val lonDiff = lon1 - lon2
    return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff)
}

// Функция для получения границ всех мест
private fun getBoundsForPlaces(places: List<ItemFromDB>): BoundingBox? {
    var minLat = 90.0
    var maxLat = -90.0
    var minLon = 180.0
    var maxLon = -180.0
    var hasValidPoints = false

    places.forEach { place ->
        val lat = place.geopoint_latitude.toDoubleOrNull()
        val lon = place.geopoint_longtitude.toDoubleOrNull()

        if (lat != null && lon != null) {
            minLat = minOf(minLat, lat)
            maxLat = maxOf(maxLat, lat)
            minLon = minOf(minLon, lon)
            maxLon = maxOf(maxLon, lon)
            hasValidPoints = true
        }
    }

    return if (hasValidPoints) {
        BoundingBox(
            southwest = Point(minLat, minLon),
            northeast = Point(maxLat, maxLon)
        )
    } else {
        null
    }
}

// Класс для хранения границ
private data class BoundingBox(
    val southwest: Point,
    val northeast: Point
) {
    val center: Point
        get() = Point(
            (southwest.latitude + northeast.latitude) / 2,
            (southwest.longitude + northeast.longitude) / 2
        )
}

// Функция для расчета масштаба
private fun calculateZoomFromBounds(bounds: Pair<Point, Point>): Float {
    val latDiff = bounds.second.latitude - bounds.first.latitude
    val lonDiff = bounds.second.longitude - bounds.first.longitude

    // Добавляем padding для лучшего отображения
    val paddedLatDiff = latDiff * 1.2
    val paddedLonDiff = lonDiff * 1.2

    return when {
        paddedLatDiff > 1.0 || paddedLonDiff > 1.0 -> 8.0f
        paddedLatDiff > 0.5 || paddedLonDiff > 0.5 -> 9.0f
        paddedLatDiff > 0.2 || paddedLonDiff > 0.2 -> 10.0f
        paddedLatDiff > 0.1 || paddedLonDiff > 0.1 -> 11.0f
        paddedLatDiff > 0.05 || paddedLonDiff > 0.05 -> 12.0f
        paddedLatDiff > 0.02 || paddedLonDiff > 0.02 -> 13.0f
        paddedLatDiff > 0.01 || paddedLonDiff > 0.01 -> 14.0f
        paddedLatDiff > 0.005 || paddedLonDiff > 0.005 -> 15.0f
        else -> 16.0f
    }
}

private fun calculateBounds(points: List<Point>): Pair<Point, Point>? {
    if (points.isEmpty()) return null

    var minLat = Double.MAX_VALUE
    var maxLat = Double.MIN_VALUE
    var minLon = Double.MAX_VALUE
    var maxLon = Double.MIN_VALUE

    points.forEach { point ->
        minLat = minOf(minLat, point.latitude)
        maxLat = maxOf(maxLat, point.latitude)
        minLon = minOf(minLon, point.longitude)
        maxLon = maxOf(maxLon, point.longitude)
    }

    return Pair(Point(minLat, minLon), Point(maxLat, maxLon))
}

private fun createCircleBitmap(context: Context, color: Int): android.graphics.Bitmap {
    val size = 20 // Размер в пикселях
    val bitmap = android.graphics.Bitmap.createBitmap(size, size, android.graphics.Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    val paint = android.graphics.Paint().apply {
        this.color = color
        style = android.graphics.Paint.Style.FILL
        isAntiAlias = true
    }

    // Рисуем круг
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

    return bitmap
}

private fun findOptimalRouteImproved(
    points: List<Point>,
    places: List<ItemFromDB>
): Pair<List<Point>, List<ItemFromDB>> {
    if (points.size <= 3) return Pair(points, places) // Для малого количества точек оптимизация не нужна

    // Сначала используем алгоритм ближайшего соседа для начального маршрута
    val routeIndices = getNearestNeighborRoute(points)

    // Затем применяем 2-opt оптимизацию для устранения пересечений
    val optimizedRouteIndices = apply2OptOptimization(points, routeIndices)

    // Формируем оптимизированные списки точек и мест
    val optimizedPoints = optimizedRouteIndices.map { points[it] }
    val optimizedPlaces = optimizedRouteIndices.map { places[it] }

    return Pair(optimizedPoints, optimizedPlaces)
}

// Получаем начальный маршрут с использованием алгоритма ближайшего соседа
private fun getNearestNeighborRoute(points: List<Point>): List<Int> {
    val n = points.size
    val visited = BooleanArray(n) { false }
    val route = mutableListOf<Int>()

    // Начинаем с первой точки
    var current = 0
    route.add(current)
    visited[current] = true

    // Находим остальные точки в порядке ближайшего соседа
    while (route.size < n) {
        var nearestIdx = -1
        var minDist = Double.MAX_VALUE

        for (i in 0 until n) {
            if (!visited[i]) {
                val dist = calculateDistance(
                    points[current].latitude, points[current].longitude,
                    points[i].latitude, points[i].longitude
                )

                if (dist < minDist) {
                    minDist = dist
                    nearestIdx = i
                }
            }
        }

        if (nearestIdx != -1) {
            current = nearestIdx
            route.add(current)
            visited[current] = true
        } else {
            break
        }
    }

    return route
}

// Применяем 2-opt оптимизацию для улучшения маршрута
private fun apply2OptOptimization(points: List<Point>, route: List<Int>): List<Int> {
    val n = route.size
    val optimizedRoute = route.toMutableList()
    var improvement = true

    // Максимальное количество итераций для предотвращения бесконечного цикла
    val maxIterations = 100
    var iteration = 0

    while (improvement && iteration < maxIterations) {
        improvement = false
        iteration++

        // Перебираем все возможные пары ребер
        for (i in 0 until n - 2) {
            for (j in i + 2 until n) {
                // Проверяем, уменьшится ли общая длина маршрута, если мы поменяем местами эти ребра
                // Текущие ребра: (i, i+1) и (j, j+1 or 0)
                val nextJ = if (j == n - 1) 0 else j + 1

                val currentDistance = calculateDistance(
                    points[optimizedRoute[i]].latitude, points[optimizedRoute[i]].longitude,
                    points[optimizedRoute[i + 1]].latitude, points[optimizedRoute[i + 1]].longitude
                ) + calculateDistance(
                    points[optimizedRoute[j]].latitude, points[optimizedRoute[j]].longitude,
                    points[optimizedRoute[nextJ]].latitude, points[optimizedRoute[nextJ]].longitude
                )

                val newDistance = calculateDistance(
                    points[optimizedRoute[i]].latitude, points[optimizedRoute[i]].longitude,
                    points[optimizedRoute[j]].latitude, points[optimizedRoute[j]].longitude
                ) + calculateDistance(
                    points[optimizedRoute[i + 1]].latitude, points[optimizedRoute[i + 1]].longitude,
                    points[optimizedRoute[nextJ]].latitude, points[optimizedRoute[nextJ]].longitude
                )

                // Если новый маршрут короче, меняем ребра
                if (newDistance < currentDistance) {
                    // Переворачиваем участок пути между i+1 и j
                    val segment = optimizedRoute.subList(i + 1, j + 1)
                    segment.reverse()
                    improvement = true
                }
            }
        }
    }

    return optimizedRoute
}