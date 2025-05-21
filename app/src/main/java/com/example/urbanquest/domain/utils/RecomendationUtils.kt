package com.example.urbanquest.domain.utils

import android.util.Log
import com.example.urbanquest.domain.model.ItemFromDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine

//Вспомогательные функции для теста рекомендаций
//Отладка

suspend fun fetchRecommendations2(selectedTags: List<String>): List<ItemFromDB> {
    val firebaseDatabase = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
    val walkingPlacesRef = firebaseDatabase.getReference("walking_places_info")
    val cafesAndRestaurantsRef = firebaseDatabase.getReference("cafes_and_restaurants")

    val walkingPlaces = searchInDatabaseWithTags(walkingPlacesRef, selectedTags)
    val cafesAndRestaurants = searchInDatabaseWithTags(cafesAndRestaurantsRef, selectedTags)

    val combinedResults = walkingPlaces + cafesAndRestaurants

    Log.d("FetchRecommendations", "Walking places found: ${walkingPlaces.size}")
    Log.d("FetchRecommendations", "Cafes and restaurants found: ${cafesAndRestaurants.size}")
    Log.d("FetchRecommendations", "Total combined results: ${combinedResults.size}")

    return combinedResults.sortedByDescending { it.matchCount }
}

private suspend fun searchInDatabaseWithTags(reference: DatabaseReference, tags: List<String>): List<ItemFromDB> {
    return suspendCancellableCoroutine { continuation ->
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<ItemFromDB>()
                if (snapshot.exists()) {
                    for (items in snapshot.children) {
                        val item = items.getValue(ItemFromDB::class.java)
                        if (item != null) {
                            val matches = countTagMatches(item, tags)
                            Log.d("DatabaseSearch", "Item: ${item.name}, Matches: $matches, Tags: ${item.tags?.values}") // Логгирование
                            if (matches > 0) {
                                item.matchCount = matches
                                result.add(item)
                            }
                        }
                    }
                }
                Log.d("DatabaseSearch", "Found ${result.size} items with matching tags")
                continuation.resume(result) { }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseSearch", "$error")
                continuation.resume(emptyList()) { }
            }
        })
    }
}

private fun countTagMatches(item: ItemFromDB, selectedTags: List<String>): Int {
    if (selectedTags.isEmpty() || item.tags.isNullOrEmpty()) return 0

    var totalScore = 0

    // Проверяем каждый выбранный тег
    for (selectedTag in selectedTags) {
        var bestMatchScore = 0

        // Проверяем против каждого тега элемента
        for (itemTag in item.tags.values) {
            val score = calculateTagMatchScore(selectedTag, itemTag)
            bestMatchScore = maxOf(bestMatchScore, score)
        }

        totalScore += bestMatchScore
    }

    Log.d("CountTagMatches", "Item: ${item.name}, Total Score: $totalScore, Selected Tags: $selectedTags")
    return totalScore
}

private fun calculateTagMatchScore(selectedTag: String, itemTag: String): Int {
    val normalizedSelectedTag = selectedTag.trim().lowercase()
    val normalizedItemTag = itemTag.trim().lowercase()

    if (normalizedSelectedTag == normalizedItemTag) {
        return 3
    }

    if (normalizedSelectedTag.contains(normalizedItemTag) ||
        normalizedItemTag.contains(normalizedSelectedTag)) {
        return 2
    }

    val keywords = normalizedSelectedTag.split(" ")
    for (keyword in keywords) {
        if (keyword.length > 3 && normalizedItemTag.contains(keyword)) {
            return 1
        }
    }

    return 0
}

