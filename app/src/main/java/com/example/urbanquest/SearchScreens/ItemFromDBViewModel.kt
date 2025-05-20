package com.example.urbanquest.SearchScreens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urbanquest.SearchScreens.data.ItemFromDB
import kotlinx.coroutines.launch

//ViewModel для списка? Не помню


class ItemFromDBViewModel : ViewModel() {
    private val _selectedPlace = MutableLiveData<ItemFromDB?>()
    val selectedPlace: LiveData<ItemFromDB?> = _selectedPlace

    // Добавляем кэширование данных
    private val walkingPlacesCache = mutableListOf<ItemFromDB>()
    private val foodPlacesCache = mutableListOf<ItemFromDB>()

    private val _isLoadingWalking = MutableLiveData(false)
    val isLoadingWalking: LiveData<Boolean> = _isLoadingWalking

    private val _isLoadingFood = MutableLiveData(false)
    val isLoadingFood: LiveData<Boolean> = _isLoadingFood

    fun selectPlace(place: ItemFromDB) {
        _selectedPlace.value = place
    }

    fun getWalkingPlaces(forceRefresh: Boolean = false): List<ItemFromDB> {
        if (forceRefresh || walkingPlacesCache.isEmpty()) {
            _isLoadingWalking.value = true
            viewModelScope.launch {
                try {
                    val places = fetchWalkingPlaces()
                    walkingPlacesCache.clear()
                    walkingPlacesCache.addAll(places)
                    _isLoadingWalking.value = false
                } catch (e: Exception) {
                    Log.e("ItemFromDBViewModel", "Error fetching walking places: ${e.message}")
                    _isLoadingWalking.value = false
                }
            }
        }
        return walkingPlacesCache
    }

    fun getFoodPlaces(forceRefresh: Boolean = false): List<ItemFromDB> {
        if (forceRefresh || foodPlacesCache.isEmpty()) {
            _isLoadingFood.value = true
            viewModelScope.launch {
                try {
                    val places = fetchFoodPlaces()
                    foodPlacesCache.clear()
                    foodPlacesCache.addAll(places)
                    _isLoadingFood.value = false
                } catch (e: Exception) {
                    Log.e("ItemFromDBViewModel", "Error fetching food places: ${e.message}")
                    _isLoadingFood.value = false
                }
            }
        }
        return foodPlacesCache
    }

    fun clearSelectedPlace() {
        _selectedPlace.value = ItemFromDB()
    }

    fun clearCache() {
        walkingPlacesCache.clear()
        foodPlacesCache.clear()
    }
}
