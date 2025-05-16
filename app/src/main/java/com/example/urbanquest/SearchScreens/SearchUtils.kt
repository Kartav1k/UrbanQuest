package com.example.urbanquest.SearchScreens

import android.util.Log
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
import java.time.LocalDate
import java.time.LocalTime

val close_place="Закрыто"
val open_place="Открыто с "
val unlimited_access="Без ограничений"

//Вспомогательные функции

fun isOpen(working: Map<String, WorkingTime>): String {
    val currentDay = LocalDate.now().dayOfWeek.toString().lowercase()
    val currentTime = LocalTime.now()
    val workingTime = working[currentDay] ?: return close_place
    if (workingTime.time_open.isEmpty() || workingTime.time_close.isEmpty()) {
        return close_place
    }
    if (workingTime.time_open == "Без ограничений" || workingTime.time_close == "Без ограничений") {
        return unlimited_access
    }
    val openTime = LocalTime.parse(workingTime.time_open)
    val closeTime = LocalTime.parse(workingTime.time_close)
    if (closeTime.isBefore(openTime)) {
        if (currentTime.isAfter(openTime) || currentTime.isBefore(closeTime)) {
            return "$open_place${workingTime.time_open}"
        }
    } else {
        if (currentTime.isAfter(openTime) && currentTime.isBefore(closeTime)) {
            return "$open_place${workingTime.time_open}"
        }
    }

    return close_place
}
suspend fun fetchFoodPlaces(): List<ItemFromDB> {
    val firebaseDatabase = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
    val cafesAndRestaurantsRef = firebaseDatabase.getReference("cafes_and_restaurants")
    return withContext(Dispatchers.IO) {
        searchInDatabase(cafesAndRestaurantsRef, "")
    }
}
suspend fun fetchWalkingPlaces(): List<ItemFromDB> {
    val firebaseDatabase = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
    val WalkingPlacesRef = firebaseDatabase.getReference("walking_places_info")
    return withContext(Dispatchers.IO) {
        searchInDatabase(WalkingPlacesRef, "")
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
