package com.example.urbanquest

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.urbanquest.domain.model.ItemFromDB
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.runtime.image.ImageProvider
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object MapUtils {

    private const val TAG = "MapUtils"
    private const val CLUSTER_RADIUS = 800.0
    private const val INTERMEDIATE_POINT_THRESHOLD = 0.3

    fun createYandexStyleLocationIcon(
        context: Context,
        bearing: Float? = null
    ): android.graphics.Bitmap {
        val size = 56
        val bitmap = android.graphics.Bitmap.createBitmap(
            size, size,
            android.graphics.Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(bitmap)

        val centerX = size / 2f
        val centerY = size / 2f

        if (bearing != null) {
            canvas.save()
            canvas.rotate(bearing, centerX, centerY)
        }

        val outerPaint = Paint().apply {
            color = Color.White.toArgb()
            style = Paint.Style.FILL
            isAntiAlias = true
            setShadowLayer(4f, 0f, 2f, Color.Black.copy(alpha = 0.25f).toArgb())
        }

        val innerPaint = Paint().apply {
            color = Color(0xFF007AFF).toArgb()
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        canvas.drawCircle(centerX, centerY, size / 2f - 2f, outerPaint)
        canvas.drawCircle(centerX, centerY, size / 2f - 5f, innerPaint)

        if (bearing != null) {
            val arrowPaint = Paint().apply {
                color = Color.White.toArgb()
                style = Paint.Style.FILL
                isAntiAlias = true
            }

            val arrowPath = Path().apply {
                moveTo(centerX, centerY - 14f)
                lineTo(centerX - 7f, centerY + 6f)
                lineTo(centerX + 7f, centerY + 6f)
                close()
            }

            canvas.drawPath(arrowPath, arrowPaint)
            canvas.restore()
        } else {
            val centerDotPaint = Paint().apply {
                color = Color.White.toArgb()
                style = Paint.Style.FILL
                isAntiAlias = true
            }

            canvas.drawCircle(centerX, centerY, 5f, centerDotPaint)
        }

        return bitmap
    }

    fun createPlaceIcon(
        context: Context,
        color: Int = Color(0xFF2196F3).toArgb(),
        size: Int = 32
    ): android.graphics.Bitmap {
        val bitmap = android.graphics.Bitmap.createBitmap(
            size, size,
            android.graphics.Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(bitmap)

        val paint = Paint().apply {
            this.color = color
            style = Paint.Style.FILL
            isAntiAlias = true
            setShadowLayer(2f, 0f, 1f, Color.Black.copy(alpha = 0.2f).toArgb())
        }
        val borderPaint = Paint().apply {
            this.color = Color.White.toArgb()
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val centerX = size / 2f
        val centerY = size / 2f

        canvas.drawCircle(centerX, centerY, size / 2f - 1f, borderPaint)
        canvas.drawCircle(centerX, centerY, size / 2f - 3f, paint)

        return bitmap
    }

    fun updateLocationIcon(
        mapObjects: MapObjectCollection,
        currentPlacemark: PlacemarkMapObject?,
        location: Point,
        bearing: Float?,
        context: Context
    ): PlacemarkMapObject {
        currentPlacemark?.let { oldPlacemark ->
            oldPlacemark.parent?.remove(oldPlacemark)
        }
        val iconBitmap = createYandexStyleLocationIcon(context, bearing)

        return mapObjects.addPlacemark(location).apply {
            setIcon(ImageProvider.fromBitmap(iconBitmap))
            setIconStyle(IconStyle().apply {
                anchor = PointF(0.5f, 0.5f)
                scale = 1.0f
                zIndex = 150.0f
            })
        }
    }

    fun setupLocationManager(
        context: Context,
        locationListener: LocationListener
    ): LocationManager? {
        return try {
            val locationManager = MapKitFactory.getInstance().createLocationManager()
            locationManager.requestSingleUpdate(locationListener)
            locationManager.subscribeForLocationUpdates(
                3.0,
                2000L,
                1.5,
                false,
                FilteringMode.ON,
                locationListener
            )

            Log.d(TAG, "LocationManager setup complete")
            locationManager
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up LocationManager: ${e.message}")
            null
        }
    }

    fun requestSingleLocationUpdate(
        locationManager: LocationManager?,
        context: Context,
        onLocationReceived: (Point, Float?) -> Unit,
        onLocationError: () -> Unit
    ) {
        if (locationManager == null) {
            onLocationError()
            return
        }

        val singleUpdateListener = object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                val userPoint = location.position
                val bearing = location.heading?.toFloat()
                Log.d(TAG, "Single location update: lat=${userPoint.latitude}, lon=${userPoint.longitude}")
                onLocationReceived(userPoint, bearing)
            }

            override fun onLocationStatusUpdated(locationStatus: LocationStatus) {
                Log.d(TAG, "Single update status: $locationStatus")
                if (locationStatus == LocationStatus.NOT_AVAILABLE) {
                    onLocationError()
                }
            }
        }

        locationManager.requestSingleUpdate(singleUpdateListener)
    }

    fun addMarkersAndRoute(
        context: Context,
        places: List<ItemFromDB>,
        mapObjects: MapObjectCollection,
        onPlaceSelected: (ItemFromDB) -> Unit,
        onDistanceUpdated: (String) -> Unit,
        userLocation: Point? = null
    ) {
        if (places.isEmpty()) {
            Log.d(TAG, "No places to add markers for")
            return
        }

        val points = mutableListOf<Point>()
        val placesForPoints = mutableListOf<ItemFromDB>()

        places.forEach { place ->
            val latitude = place.geopoint_latitude.toDoubleOrNull()
            val longitude = place.geopoint_longtitude.toDoubleOrNull()

            if (latitude != null && longitude != null) {
                val point = Point(latitude, longitude)
                points.add(point)
                placesForPoints.add(place)
            }
        }

        if (points.isEmpty()) return

        val optimizedRoute = buildOptimalMoscowRoute(points, placesForPoints, userLocation)
        val optimizedPoints = optimizedRoute.first
        val optimizedPlaces = optimizedRoute.second

        if (optimizedPoints.size >= 2) {
            val polyline = Polyline(optimizedPoints)
            val polylineObject = mapObjects.addPolyline(polyline)
            polylineObject.setStrokeColor(Color(0xFF4CAF50).toArgb())
            polylineObject.strokeWidth = 4f
            polylineObject.zIndex = 10f
        }

        optimizedPoints.forEachIndexed { index, point ->
            val place = optimizedPlaces[index]
            val placemark = mapObjects.addPlacemark(point)

            val iconBitmap = createPlaceIcon(
                context = context,
                color = Color(0xFF2196F3).toArgb(),
                size = 32
            )

            placemark.setIcon(ImageProvider.fromBitmap(iconBitmap))
            placemark.setIconStyle(IconStyle().apply {
                anchor = PointF(0.5f, 0.5f)
                scale = 1.0f
                zIndex = 15.0f
            })

            placemark.setText(place.name, TextStyle().apply {
                placement = TextStyle.Placement.BOTTOM
                offset = 5f
                color = Color.Black.toArgb()
                size = 11f
            })

            placemark.addTapListener(MapObjectTapListener { _, _ ->
                onPlaceSelected(place)
                true
            })
        }
    }

    fun addSinglePlaceMarker(
        context: Context,
        mapObjects: MapObjectCollection,
        point: Point
    ) {
        val imageProvider = ImageProvider.fromResource(context, R.drawable.label_user)
        mapObjects.addPlacemark(point).apply {
            setIcon(imageProvider)
            setIconStyle(IconStyle().apply {
                anchor = PointF(0.5f, 1.0f)
                scale = 0.6f
                zIndex = 10.0f
            })
        }
    }

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000

        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val deltaLatRad = Math.toRadians(lat2 - lat1)
        val deltaLonRad = Math.toRadians(lon2 - lon1)

        val a = sin(deltaLatRad / 2).pow(2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(deltaLonRad / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    fun calculateSimpleDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return calculateDistance(lat1, lon1, lat2, lon2) / 1000.0
    }

    fun calculateBounds(points: List<Point>): Pair<Point, Point>? {
        if (points.isEmpty()) return null

        var minLat = Double.MAX_VALUE
        var maxLat = Double.MIN_VALUE
        var minLon = Double.MAX_VALUE
        var maxLon = Double.MIN_VALUE

        points.forEach { point ->
            minLat = min(minLat, point.latitude)
            maxLat = max(maxLat, point.latitude)
            minLon = min(minLon, point.longitude)
            maxLon = max(maxLon, point.longitude)
        }

        return Pair(Point(minLat, minLon), Point(maxLat, maxLon))
    }

    fun calculateZoomFromBounds(bounds: Pair<Point, Point>): Float {
        val latDiff = bounds.second.latitude - bounds.first.latitude
        val lonDiff = bounds.second.longitude - bounds.first.longitude
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

    private data class PointCluster(
        val points: MutableList<Int> = mutableListOf(),
        val centerLat: Double,
        val centerLon: Double
    )

    private fun buildOptimalMoscowRoute(
        points: List<Point>,
        places: List<ItemFromDB>,
        userLocation: Point? = null
    ): Pair<List<Point>, List<ItemFromDB>> {

        if (points.size <= 1) {
            return Pair(points, places)
        }

        if (points.size == 2) {
            return if (userLocation != null) {
                optimizeStartPoint(points, places, userLocation)
            } else {
                val optimalStart = findGeographicallyOptimalStart(points)
                reorderFromStart(points, places, optimalStart)
            }
        }

        if (points.size == 3) {
            return if (userLocation != null) {
                val optimizedByLocation = optimizeStartPoint(points, places, userLocation)
                val optimizedByRoute = optimizeThreePointRoute(points, places)
                val distanceByLocation = calculateTotalRouteDistance(optimizedByLocation.first)
                val distanceByRoute = calculateTotalRouteDistance(optimizedByRoute.first)
                if (distanceByRoute < distanceByLocation * 0.9) {
                    optimizedByRoute
                } else {
                    optimizedByLocation
                }
            } else {
                optimizeThreePointRoute(points, places)
            }
        }
        val clusters = identifyClusters(points)
        val startIndex = if (userLocation != null) {
            findOptimalStartPoint(points, userLocation, clusters)
        } else {
            findSmartStartPoint(points, clusters)
        }

        val route = buildClusterAwareRoute(points, clusters, startIndex)
        val optimizedRoute = applyAdvancedOptimizations(points, route)

        val finalPoints = optimizedRoute.map { points[it] }
        val finalPlaces = optimizedRoute.map { places[it] }

        val totalDistance = calculateTotalRouteDistance(finalPoints)
        return Pair(finalPoints, finalPlaces)
    }

    private fun findGeographicallyOptimalStart(points: List<Point>): Int {
        val centerLat = points.map { it.latitude }.average()
        val centerLon = points.map { it.longitude }.average()
        return points.indices.maxByOrNull { index ->
            calculateDistance(
                centerLat, centerLon,
                points[index].latitude, points[index].longitude
            )
        } ?: 0
    }

    private fun findSmartStartPoint(points: List<Point>, clusters: List<PointCluster>): Int {
        var bestStartIndex = 0
        var bestTotalDistance = Double.MAX_VALUE
        val testPoints = if (points.size <= 8) {
            points.indices.toList()
        } else {
            findCandidateStartPoints(points, clusters)
        }

        testPoints.forEach { candidateStart ->
            val testRoute = buildClusterAwareRoute(points, clusters, candidateStart)
            val testDistance = calculateTotalRouteDistance(testRoute.map { points[it] })

            if (testDistance < bestTotalDistance) {
                bestTotalDistance = testDistance
                bestStartIndex = candidateStart
            }
        }

        Log.d(TAG, "Smart start point selection: tested ${testPoints.size} candidates, best distance: ${String.format("%.1f", bestTotalDistance)} km")
        return bestStartIndex
    }

    private fun findCandidateStartPoints(points: List<Point>, clusters: List<PointCluster>): List<Int> {
        val candidates = mutableSetOf<Int>()
        val centerLat = points.map { it.latitude }.average()
        val centerLon = points.map { it.longitude }.average()
        candidates.add(points.indices.maxByOrNull { points[it].latitude } ?: 0)
        candidates.add(points.indices.minByOrNull { points[it].latitude } ?: 0)
        candidates.add(points.indices.maxByOrNull { points[it].longitude } ?: 0)
        candidates.add(points.indices.minByOrNull { points[it].longitude } ?: 0)
        clusters.forEach { cluster ->
            candidates.add(cluster.points.first())
        }
        points.indices.forEach { pointIndex ->
            val isInCluster = clusters.any { cluster -> cluster.points.contains(pointIndex) }
            if (!isInCluster) {
                candidates.add(pointIndex)
            }
        }

        return candidates.take(5)
    }

    private fun reorderFromStart(
        points: List<Point>,
        places: List<ItemFromDB>,
        startIndex: Int
    ): Pair<List<Point>, List<ItemFromDB>> {
        if (startIndex == 0) return Pair(points, places)

        val reorderedPoints = mutableListOf<Point>()
        val reorderedPlaces = mutableListOf<ItemFromDB>()

        for (i in points.indices) {
            val index = (startIndex + i) % points.size
            reorderedPoints.add(points[index])
            reorderedPlaces.add(places[index])
        }

        return Pair(reorderedPoints, reorderedPlaces)
    }

    private fun identifyClusters(points: List<Point>): List<PointCluster> {
        val clusters = mutableListOf<PointCluster>()
        val assigned = BooleanArray(points.size) { false }

        for (i in points.indices) {
            if (assigned[i]) continue

            val cluster = PointCluster(
                centerLat = points[i].latitude,
                centerLon = points[i].longitude
            )
            cluster.points.add(i)
            assigned[i] = true

            for (j in i + 1 until points.size) {
                if (assigned[j]) continue

                val distance = calculateDistance(
                    points[i].latitude, points[i].longitude,
                    points[j].latitude, points[j].longitude
                )

                if (distance <= CLUSTER_RADIUS) {
                    cluster.points.add(j)
                    assigned[j] = true
                }
            }

            clusters.add(cluster)
        }

        return clusters
    }

    private fun findOptimalStartPoint(
        points: List<Point>,
        userLocation: Point?,
        clusters: List<PointCluster>
    ): Int {
        return if (userLocation != null) {
            var nearestIndex = 0
            var minDistance = Double.MAX_VALUE

            points.forEachIndexed { index, point ->
                val distance = calculateDistance(
                    userLocation.latitude, userLocation.longitude,
                    point.latitude, point.longitude
                )
                if (distance < minDistance) {
                    minDistance = distance
                    nearestIndex = index
                }
            }
            nearestIndex
        } else {
            findSmartStartPoint(points, clusters)
        }
    }

    private fun findMostCentralPoint(points: List<Point>, clusters: List<PointCluster>): Int {
        val avgLat = points.map { it.latitude }.average()
        val avgLon = points.map { it.longitude }.average()

        var centralIndex = 0
        var minDistance = Double.MAX_VALUE

        points.forEachIndexed { index, point ->
            val distance = calculateDistance(avgLat, avgLon, point.latitude, point.longitude)
            if (distance < minDistance) {
                minDistance = distance
                centralIndex = index
            }
        }

        return centralIndex
    }

    private fun buildClusterAwareRoute(
        points: List<Point>,
        clusters: List<PointCluster>,
        startIndex: Int
    ): List<Int> {
        val route = mutableListOf<Int>()
        val visited = BooleanArray(points.size) { false }
        val visitedClusters = BooleanArray(clusters.size) { false }

        var currentIndex = startIndex
        route.add(currentIndex)
        visited[currentIndex] = true

        while (route.size < points.size) {
            val nextTarget = findBestNextTarget(points, currentIndex, visited, clusters, visitedClusters)

            if (nextTarget.isCluster) {
                val clusterIndex = nextTarget.clusterIndex
                val clusterPoints = clusters[clusterIndex].points.filter { !visited[it] }

                val entryPoint = findBestClusterEntryPoint(points, currentIndex, clusterPoints)
                val orderedClusterPoints = planClusterTraversal(points, clusterPoints, entryPoint, currentIndex)

                orderedClusterPoints.forEach { pointIndex ->
                    if (!visited[pointIndex]) {
                        route.add(pointIndex)
                        visited[pointIndex] = true
                        currentIndex = pointIndex
                    }
                }
                visitedClusters[clusterIndex] = true
            } else {
                currentIndex = nextTarget.pointIndex
                route.add(currentIndex)
                visited[currentIndex] = true
            }
        }

        return route
    }

    private data class NextTarget(
        val isCluster: Boolean,
        val clusterIndex: Int = -1,
        val pointIndex: Int = -1,
        val score: Double = Double.MAX_VALUE
    )

    private fun findClusterForPoint(pointIndex: Int, clusters: List<PointCluster>): Int {
        clusters.forEachIndexed { clusterIndex, cluster ->
            if (cluster.points.contains(pointIndex)) {
                return clusterIndex
            }
        }
        return -1
    }

    private fun orderPointsInCluster(
        points: List<Point>,
        clusterPoints: List<Int>,
        startFromIndex: Int
    ): List<Int> {
        if (clusterPoints.size <= 1) return clusterPoints

        val ordered = mutableListOf<Int>()
        val remaining = clusterPoints.toMutableList()
        var current = startFromIndex

        while (remaining.isNotEmpty()) {
            val nearest = remaining.minByOrNull { pointIndex ->
                calculateDistance(
                    points[current].latitude, points[current].longitude,
                    points[pointIndex].latitude, points[pointIndex].longitude
                )
            }

            if (nearest != null) {
                ordered.add(nearest)
                remaining.remove(nearest)
                current = nearest
            } else {
                break
            }
        }

        return ordered
    }

    private fun findBestNextTarget(
        points: List<Point>,
        currentIndex: Int,
        visited: BooleanArray,
        clusters: List<PointCluster>,
        visitedClusters: BooleanArray
    ): NextTarget {
        val candidates = mutableListOf<NextTarget>()
        for (clusterIndex in clusters.indices) {
            if (visitedClusters[clusterIndex]) continue

            val cluster = clusters[clusterIndex]
            val unvisitedInCluster = cluster.points.filter { !visited[it] }
            if (unvisitedInCluster.isEmpty()) continue

            val entryPoint = findBestClusterEntryPoint(points, currentIndex, unvisitedInCluster)
            val distanceToCluster = calculateDistance(
                points[currentIndex].latitude, points[currentIndex].longitude,
                points[entryPoint].latitude, points[entryPoint].longitude
            )
            val clusterBonus = unvisitedInCluster.size * 200.0
            val score = distanceToCluster - clusterBonus

            candidates.add(NextTarget(
                isCluster = true,
                clusterIndex = clusterIndex,
                score = score
            ))
        }
        for (pointIndex in points.indices) {
            if (visited[pointIndex]) continue

            val pointCluster = findClusterForPoint(pointIndex, clusters)
            if (pointCluster != -1 && !visitedClusters[pointCluster]) continue

            val distance = calculateDistance(
                points[currentIndex].latitude, points[currentIndex].longitude,
                points[pointIndex].latitude, points[pointIndex].longitude
            )

            candidates.add(NextTarget(
                isCluster = false,
                pointIndex = pointIndex,
                score = distance
            ))
        }

        return candidates.minByOrNull { it.score } ?: NextTarget(false, pointIndex = -1)
    }

    private fun findBestClusterEntryPoint(
        points: List<Point>,
        currentIndex: Int,
        clusterPoints: List<Int>
    ): Int {
        return clusterPoints.minByOrNull { pointIndex ->
            calculateDistance(
                points[currentIndex].latitude, points[currentIndex].longitude,
                points[pointIndex].latitude, points[pointIndex].longitude
            )
        } ?: clusterPoints.first()
    }

    private fun planClusterTraversal(
        points: List<Point>,
        clusterPoints: List<Int>,
        entryPoint: Int,
        approachFromIndex: Int
    ): List<Int> {
        if (clusterPoints.size <= 1) return clusterPoints
        val result = mutableListOf<Int>()
        val remaining = clusterPoints.toMutableList()
        var current = entryPoint
        result.add(current)
        remaining.remove(current)
        val traversalOrder = if (remaining.size >= 2) {
            optimizeClusterOrder(points, remaining, current, approachFromIndex)
        } else {
            remaining
        }

        result.addAll(traversalOrder)
        return result
    }

    private fun optimizeClusterOrder(
        points: List<Point>,
        clusterPoints: List<Int>,
        startPoint: Int,
        approachFrom: Int
    ): List<Int> {
        if (clusterPoints.size <= 2) {
            return clusterPoints.sortedBy { pointIndex ->
                calculateDistance(
                    points[startPoint].latitude, points[startPoint].longitude,
                    points[pointIndex].latitude, points[pointIndex].longitude
                )
            }
        }
        val centerLat = clusterPoints.map { points[it].latitude }.average()
        val centerLon = clusterPoints.map { points[it].longitude }.average()
        val approachAngle = calculateBearing(
            points[approachFrom].latitude, points[approachFrom].longitude,
            points[startPoint].latitude, points[startPoint].longitude
        )
        val sortedByAngle = clusterPoints.sortedBy { pointIndex ->
            val pointAngle = calculateBearing(
                centerLat, centerLon,
                points[pointIndex].latitude, points[pointIndex].longitude
            )
            var normalizedAngle = pointAngle - approachAngle
            if (normalizedAngle < 0) normalizedAngle += 2 * Math.PI
            if (normalizedAngle > 2 * Math.PI) normalizedAngle -= 2 * Math.PI
            normalizedAngle
        }

        return sortedByAngle
    }

    private fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val deltaLon = Math.toRadians(lon2 - lon1)

        val y = sin(deltaLon) * cos(lat2Rad)
        val x = cos(lat1Rad) * sin(lat2Rad) - sin(lat1Rad) * cos(lat2Rad) * cos(deltaLon)

        return atan2(y, x)
    }

    private fun findIntermediatePoints(
        points: List<Point>,
        fromIndex: Int,
        toIndex: Int,
        visited: BooleanArray
    ): Int {
        val unvisited = points.indices.filter { !visited[it] && it != toIndex }
        if (unvisited.isEmpty()) return 0

        val directDistance = calculateDistance(
            points[fromIndex].latitude, points[fromIndex].longitude,
            points[toIndex].latitude, points[toIndex].longitude
        )

        return unvisited.count { intermediateIndex ->
            val distanceToIntermediate = calculateDistance(
                points[fromIndex].latitude, points[fromIndex].longitude,
                points[intermediateIndex].latitude, points[intermediateIndex].longitude
            )

            val distanceFromIntermediate = calculateDistance(
                points[intermediateIndex].latitude, points[intermediateIndex].longitude,
                points[toIndex].latitude, points[toIndex].longitude
            )

            val totalViaIntermediate = distanceToIntermediate + distanceFromIntermediate
            val deviation = totalViaIntermediate / directDistance

            deviation <= (1.0 + INTERMEDIATE_POINT_THRESHOLD)
        }
    }

    private fun applyAdvancedOptimizations(points: List<Point>, route: List<Int>): List<Int> {
        var optimizedRoute = route.toMutableList()

        optimizedRoute = optimizeIntermediatePoints(points, optimizedRoute)
        optimizedRoute = apply2OptImproved(points, optimizedRoute)

        return optimizedRoute
    }

    private fun optimizeIntermediatePoints(points: List<Point>, route: List<Int>): MutableList<Int> {
        val optimized = route.toMutableList()
        var madeChanges = true
        var iterations = 0

        while (madeChanges && iterations < 3) {
            madeChanges = false
            iterations++

            for (i in 0 until optimized.size - 2) {
                val from = optimized[i]
                val to = optimized[i + 2]

                for (j in i + 2 until optimized.size) {
                    val candidate = optimized[j]

                    if (isPointOnPath(points, from, to, candidate)) {
                        val element = optimized.removeAt(j)
                        optimized.add(i + 1, element)
                        madeChanges = true
                        Log.d(TAG, "Moved intermediate point from position $j to ${i + 1}")
                        break
                    }
                }
                if (madeChanges) break
            }
        }

        return optimized
    }

    private fun isPointOnPath(points: List<Point>, fromIndex: Int, toIndex: Int, candidateIndex: Int): Boolean {
        val directDistance = calculateDistance(
            points[fromIndex].latitude, points[fromIndex].longitude,
            points[toIndex].latitude, points[toIndex].longitude
        )

        val viaCandidate = calculateDistance(
            points[fromIndex].latitude, points[fromIndex].longitude,
            points[candidateIndex].latitude, points[candidateIndex].longitude
        ) + calculateDistance(
            points[candidateIndex].latitude, points[candidateIndex].longitude,
            points[toIndex].latitude, points[toIndex].longitude
        )

        return viaCandidate <= directDistance * (1.0 + INTERMEDIATE_POINT_THRESHOLD)
    }

    private fun apply2OptImproved(points: List<Point>, route: List<Int>): MutableList<Int> {
        val optimized = route.toMutableList()
        var improvement = true
        var iteration = 0

        while (improvement && iteration < 20) {
            improvement = false
            iteration++

            for (i in 0 until optimized.size - 2) {
                for (j in i + 2 until optimized.size) {
                    val currentCost = calculateSegmentCost(points, optimized, i, j)

                    val testRoute = optimized.toMutableList()
                    val segment = testRoute.subList(i + 1, j + 1)
                    segment.reverse()

                    val newCost = calculateSegmentCost(points, testRoute, i, j)

                    if (newCost < currentCost - 100) {
                        optimized.clear()
                        optimized.addAll(testRoute)
                        improvement = true
                        Log.d(TAG, "2-opt improved segment $i-$j, saved ${(currentCost - newCost).toInt()}m")
                        break
                    }
                }
                if (improvement) break
            }
        }

        return optimized
    }

    private fun calculateSegmentCost(points: List<Point>, route: List<Int>, i: Int, j: Int): Double {
        val nextI = if (i + 1 < route.size) i + 1 else 0
        val nextJ = if (j + 1 < route.size) j + 1 else 0

        return calculateDistance(
            points[route[i]].latitude, points[route[i]].longitude,
            points[route[nextI]].latitude, points[route[nextI]].longitude
        ) + calculateDistance(
            points[route[j]].latitude, points[route[j]].longitude,
            points[route[nextJ]].latitude, points[route[nextJ]].longitude
        )
    }

    private fun calculateTotalRouteDistance(points: List<Point>): Double {
        var totalDistance = 0.0

        for (i in 0 until points.size - 1) {
            totalDistance += calculateDistance(
                points[i].latitude, points[i].longitude,
                points[i + 1].latitude, points[i + 1].longitude
            )
        }

        return totalDistance / 1000.0
    }

    private fun optimizeStartPoint(
        points: List<Point>,
        places: List<ItemFromDB>,
        userLocation: Point
    ): Pair<List<Point>, List<ItemFromDB>> {
        if (points.size <= 1) return Pair(points, places)

        var nearestIndex = 0
        var minDistance = Double.MAX_VALUE

        points.forEachIndexed { index, point ->
            val distance = calculateDistance(
                userLocation.latitude, userLocation.longitude,
                point.latitude, point.longitude
            )
            if (distance < minDistance) {
                minDistance = distance
                nearestIndex = index
            }
        }

        if (nearestIndex == 0) return Pair(points, places)

        val reorderedPoints = mutableListOf<Point>()
        val reorderedPlaces = mutableListOf<ItemFromDB>()

        for (i in points.indices) {
            val index = (nearestIndex + i) % points.size
            reorderedPoints.add(points[index])
            reorderedPlaces.add(places[index])
        }

        return Pair(reorderedPoints, reorderedPlaces)
    }

    private fun optimizeThreePointRoute(
        points: List<Point>,
        places: List<ItemFromDB>
    ): Pair<List<Point>, List<ItemFromDB>> {
        if (points.size != 3) return Pair(points, places)
        val permutations = listOf(
            listOf(0, 1, 2),
            listOf(0, 2, 1),
            listOf(1, 0, 2),
            listOf(1, 2, 0),
            listOf(2, 0, 1),
            listOf(2, 1, 0)
        )
        var bestRoute = permutations[0]
        var bestDistance = Double.MAX_VALUE
        permutations.forEach { route ->
            val distance = calculateTotalRouteDistance(route.map { points[it] })
            if (distance < bestDistance) {
                bestDistance = distance
                bestRoute = route
            }
        }
        val optimizedPoints = bestRoute.map { points[it] }
        val optimizedPlaces = bestRoute.map { places[it] }

        return Pair(optimizedPoints, optimizedPlaces)
    }
}