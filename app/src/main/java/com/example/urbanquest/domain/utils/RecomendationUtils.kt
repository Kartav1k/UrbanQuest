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
    suspend fun fetchRecommendations2(selectedTags: List<String>, limit: Int = 10): Pair<List<ItemFromDB>, List<ItemFromDB>> {
        val firebaseDatabase = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/")
        val walkingPlacesRef = firebaseDatabase.getReference("walking_places_info")
        val cafesAndRestaurantsRef = firebaseDatabase.getReference("cafes_and_restaurants")

        val walkingPlaces = searchInDatabaseWithTags(walkingPlacesRef, selectedTags, limit, true)
        val cafesAndRestaurants = searchInDatabaseWithTags(cafesAndRestaurantsRef, selectedTags, limit, true)

        return Pair(walkingPlaces, cafesAndRestaurants)
    }

    private suspend fun searchInDatabaseWithTags(reference: DatabaseReference, tags: List<String>, limit: Int = 10, strictLimit: Boolean = true): List<ItemFromDB> {
        return suspendCancellableCoroutine { continuation ->
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = mutableListOf<ItemFromDB>()
                    if (snapshot.exists()) {
                        for (items in snapshot.children) {
                            val item = items.getValue(ItemFromDB::class.java)
                            if (item != null) {
                                val matches = countTagMatches(item, tags)
                                Log.d("DatabaseSearch", "Item: ${item.name}, Matches: $matches, Tags: ${item.tags?.values}")
                                if (matches > 0) {
                                    item.matchCount = matches
                                    result.add(item)
                                }
                            }
                        }
                    }
                    Log.d("DatabaseSearch", "Found ${result.size} items with matching tags")
                    val sortedResult = result.sortedWith(
                        compareByDescending<ItemFromDB> { it.matchCount }
                            .thenByDescending { it.rate }
                    )
                    val limitedResult = if (sortedResult.size > limit) {
                        val cutoffScore = sortedResult[limit - 1].matchCount
                        val itemsWithCutoffScore = sortedResult.filter { it.matchCount == cutoffScore }
                        val guaranteedItems = sortedResult.filter { it.matchCount > cutoffScore }
                        guaranteedItems + itemsWithCutoffScore
                    } else {
                        sortedResult
                    }
                    val finalResult = sortedResult.take(limit)
                    continuation.resume(finalResult) { }
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

        for (selectedTag in selectedTags) {
            var bestMatchScore = 0
            for (itemTag in item.tags.values) {
                val score = calculateTagMatchScore(selectedTag, itemTag)
                bestMatchScore = maxOf(bestMatchScore, score)
            }
            totalScore += bestMatchScore
        }
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

