package com.example.urbanquest.SearchScreens

import android.util.Log
import com.example.urbanquest.AuthorizationScreens.UserViewModel
import com.example.urbanquest.SearchScreens.data.ItemFromDB
import com.example.urbanquest.SearchScreens.data.WorkingTime
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

suspend fun searchItems(query: String): Pair<List<ItemFromDB>, List<ItemFromDB>> {
    val firebaseDatabase = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
    val walkingPlacesRef = firebaseDatabase.getReference("walking_places_info")
    val cafesAndRestaurantsRef = firebaseDatabase.getReference("cafes_and_restaurants")

    return withContext(Dispatchers.IO) {
        val walkingPlaces = searchInDatabase(walkingPlacesRef, query)
        val cafesAndRestaurants = searchInDatabase(cafesAndRestaurantsRef, query)
        Pair(walkingPlaces, cafesAndRestaurants)
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
        // Получаем список ID избранных мест
        val favoritePlaceIds = userViewModel.getFavouritePlaces()

        // Загружаем все места и фильтруем по избранным
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
