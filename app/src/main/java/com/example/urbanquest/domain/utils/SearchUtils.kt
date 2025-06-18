package com.example.urbanquest.domain.utils

import android.util.Log
import com.example.urbanquest.domain.model.ItemFromDB
import com.example.urbanquest.domain.model.WorkingTime
import com.example.urbanquest.ui.viewmodel.UserViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

val close_place="Закрыто"
val open_place="Открыто "
val unlimited_access="Без ограничений"

//Вспомогательные функции
fun isOpen(working: Map<String, WorkingTime>): String {
    val zoneId = ZoneId.of("Europe/Moscow")
    val currentDateTime = ZonedDateTime.now(zoneId)
    val currentDay = currentDateTime.dayOfWeek.toString().lowercase()
    val currentTime = currentDateTime.toLocalTime()

    val workingTime = working[currentDay] ?: return close_place

    val open = workingTime.time_open
    val close = workingTime.time_close

    if (open.isEmpty() || close.isEmpty()) return close_place

    if (open == "Без ограничений" || close == "Без ограничений") {
        return unlimited_access
    }

    val openTime = LocalTime.parse(open)
    val closeTime = LocalTime.parse(close)

    val isOpenNow = if (closeTime.isBefore(openTime)) {
        currentTime.isAfter(openTime) || currentTime.isBefore(closeTime)
    } else {
        currentTime.isAfter(openTime) && currentTime.isBefore(closeTime)
    }

    return if (isOpenNow) {
        "$open_place ($open - $close)"
    } else {
        close_place
    }
}

private suspend fun searchInDatabase(reference: DatabaseReference, query: String): List<ItemFromDB> {
    return suspendCancellableCoroutine { continuation ->
        reference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<ItemFromDB>()
                if (snapshot.exists()) {
                    for (items in snapshot.children) {
                        val item = items.getValue(ItemFromDB::class.java)
                        if (item != null && item.name?.contains(query, ignoreCase = true) == true) {
                            result.add(item)
                        }
                    }
                }
                continuation.resume(result) { }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "$error")
                continuation.resume(emptyList()) { }
            }
        })
    }
}

suspend fun fetchFoodPlaces(): List<ItemFromDB> {
    val firebaseDatabase = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
    val cafesAndRestaurantsRef = firebaseDatabase.getReference("cafes_and_restaurants")
    return withContext(Dispatchers.IO) {
        searchInDatabase(cafesAndRestaurantsRef, "")
    }
}
suspend fun fetchWalkingPlaces(): List<ItemFromDB> {
    val db = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
    val ref = db.getReference("walking_places_info")
    return withContext(Dispatchers.IO) {
        searchInDatabase(ref, "")
    }
}

suspend fun fetchFoodPlacesPaginated(
    lastKey: String? = null,
    pageSize: Int = 10
): Pair<List<ItemFromDB>, String?> {
    val firebaseDatabase = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
    val cafesAndRestaurantsRef = firebaseDatabase.getReference("cafes_and_restaurants")

    return withContext(Dispatchers.IO) {
        fetchPaginatedData(cafesAndRestaurantsRef, lastKey, pageSize)
    }
}

suspend fun fetchWalkingPlacesPaginated(
    lastKey: String? = null,
    pageSize: Int = 10
): Pair<List<ItemFromDB>, String?> {
    val firebaseDatabase = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
    val walkingPlacesRef = firebaseDatabase.getReference("walking_places_info")

    return withContext(Dispatchers.IO) {
        fetchPaginatedData(walkingPlacesRef, lastKey, pageSize)
    }
}

private suspend fun fetchPaginatedData(
    reference: DatabaseReference,
    lastKey: String?,
    pageSize: Int
): Pair<List<ItemFromDB>, String?> {
    return suspendCancellableCoroutine { continuation ->
        val query: Query = if (lastKey != null) {
            reference.orderByKey().startAfter(lastKey).limitToFirst(pageSize)
        } else {
            reference.orderByKey().limitToFirst(pageSize)
        }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<ItemFromDB>()
                var newLastKey: String? = null

                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(ItemFromDB::class.java)
                        if (item != null) {
                            result.add(item)
                            newLastKey = itemSnapshot.key
                        }
                    }
                }
                continuation.resume(Pair(result, newLastKey)) { }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching paginated data: $error")
                continuation.resume(Pair(emptyList(), null)) { }
            }
        })
    }
}


suspend fun loadFavoritePlaces(
    userViewModel: UserViewModel,
    onLoadingChange: (Boolean) -> Unit,
    onErrorChange: (String?) -> Unit,
    onWalkingPlacesLoaded: (List<ItemFromDB>) -> Unit,
    onFoodPlacesLoaded: (List<ItemFromDB>) -> Unit
) {
    onLoadingChange(true)
    onErrorChange(null)

    try {
        val favoritePlaceIds = userViewModel.getFavouritePlaces()
        val allWalkingPlaces = fetchWalkingPlaces()
        val allFoodPlaces = fetchFoodPlaces()
        val favoriteWalkingPlaces = allWalkingPlaces.filter {
            favoritePlaceIds.contains(it.id.toString())
        }
        val favoriteFoodPlaces = allFoodPlaces.filter {
            favoritePlaceIds.contains(it.id.toString())
        }
        onWalkingPlacesLoaded(favoriteWalkingPlaces)
        onFoodPlacesLoaded(favoriteFoodPlaces)
        onLoadingChange(false)
    } catch (e: Exception) {
        onErrorChange("Не удалось загрузить избранные места: ${e.message}")
        onLoadingChange(false)
    }
}

suspend fun searchItemsPaginated(
    query: String,
    lastWalkingKey: String? = null,
    lastFoodKey: String? = null,
    pageSize: Int = 10
): Triple<List<ItemFromDB>, List<ItemFromDB>, Pair<String?, String?>> {
    val firebaseDatabase = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
    val walkingPlacesRef = firebaseDatabase.getReference("walking_places_info")
    val cafesAndRestaurantsRef = firebaseDatabase.getReference("cafes_and_restaurants")

    return withContext(Dispatchers.IO) {
        val (walkingPlaces, newWalkingKey) = searchInDatabasePaginated(walkingPlacesRef, query, lastWalkingKey, pageSize)
        val (cafesAndRestaurants, newFoodKey) = searchInDatabasePaginated(cafesAndRestaurantsRef, query, lastFoodKey, pageSize)
        Triple(walkingPlaces, cafesAndRestaurants, Pair(newWalkingKey, newFoodKey))
    }
}

private suspend fun searchInDatabasePaginated(
    reference: DatabaseReference,
    query: String,
    lastKey: String?,
    pageSize: Int
): Pair<List<ItemFromDB>, String?> {
    return suspendCancellableCoroutine { continuation ->
        // Загружаем ВСЕ данные для поиска, как это делает fetchPaginatedData
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allMatchingItems = mutableListOf<ItemFromDB>()

                Log.d("SearchPaginated", "Starting search for query: '$query'")
                Log.d("SearchPaginated", "Total items in database: ${snapshot.childrenCount}")

                if (snapshot.exists()) {
                    // Ищем ВСЕ подходящие элементы (как в обычном списке мест)
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(ItemFromDB::class.java)

                        if (item != null) {
                            val itemName = item.name.takeIf { it.isNotBlank() } ?: ""
                            Log.d("SearchPaginated", "Checking item: name='$itemName', key='${itemSnapshot.key}', query='$query'")

                            if (itemName.isNotEmpty() && itemName.contains(query, ignoreCase = true)) {
                                Log.d("SearchPaginated", "Found match: $itemName")
                                allMatchingItems.add(item)
                            }
                        } else {
                            Log.w("SearchPaginated", "Item is null for key: ${itemSnapshot.key}")
                        }
                    }

                    Log.d("SearchPaginated", "Total matching items found: ${allMatchingItems.size}")

                    // Применяем пагинацию к результатам поиска
                    val startIndex = if (lastKey != null) {
                        val lastIndex = allMatchingItems.indexOfFirst { it.id.toString() == lastKey }
                        if (lastIndex >= 0) lastIndex + 1 else 0
                    } else {
                        0
                    }

                    val endIndex = minOf(startIndex + pageSize, allMatchingItems.size)
                    val paginatedResults = if (startIndex < allMatchingItems.size) {
                        allMatchingItems.subList(startIndex, endIndex)
                    } else {
                        emptyList()
                    }

                    val newLastKey = if (endIndex < allMatchingItems.size) {
                        paginatedResults.lastOrNull()?.id?.toString()
                    } else {
                        null
                    }

                    Log.d("SearchPaginated", "Returning ${paginatedResults.size} results for this page")
                    continuation.resume(Pair(paginatedResults, newLastKey)) { }
                } else {
                    Log.d("SearchPaginated", "No data in snapshot")
                    continuation.resume(Pair(emptyList(), null)) { }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error searching paginated data: $error")
                continuation.resume(Pair(emptyList(), null)) { }
            }
        })
    }
}