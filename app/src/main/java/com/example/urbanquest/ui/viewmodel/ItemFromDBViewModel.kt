package com.example.urbanquest.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urbanquest.domain.model.ItemFromDB
import com.example.urbanquest.domain.utils.fetchFoodPlaces
import com.example.urbanquest.domain.utils.fetchWalkingPlaces
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    private val _selectedForMap = MutableStateFlow<Set<Long>>(setOf())
    val selectedForMap: StateFlow<Set<Long>> = _selectedForMap

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

    // Выбрать/отменить выбор места для отображения на карте
    fun togglePlaceSelection(placeId: Long) {
        val currentSelected = _selectedForMap.value.toMutableSet()

        if (currentSelected.contains(placeId)) {
            currentSelected.remove(placeId)
            Log.d("ItemFromDBViewModel", "Removed place $placeId from selection")
        } else {
            currentSelected.add(placeId)
            Log.d("ItemFromDBViewModel", "Added place $placeId to selection")
        }

        _selectedForMap.value = currentSelected
        Log.d("ItemFromDBViewModel", "Updated selected places: ${_selectedForMap.value}")
    }


    // Проверить, выбрано ли место для отображения на карте
    fun isSelectedForMap(placeId: Long): Boolean {
        val isSelected = _selectedForMap.value.contains(placeId)
        Log.d("ItemFromDBViewModel", "Checking if place $placeId is selected: $isSelected")
        return isSelected
    }

    // Получить все выбранные места
    fun getSelectedPlaces(): List<ItemFromDB> {
        // Для отладки
        Log.d("ItemFromDBViewModel", "getSelectedPlaces called, selectedIds = ${_selectedForMap.value}")

        // Проверяем кэши и загружаем места, если они еще не загружены
        if (walkingPlacesCache.isEmpty()) {
            Log.d("ItemFromDBViewModel", "Walking places cache is empty, loading places")
            // Загружаем синхронно вместо асинхронной загрузки
            try {
                val places = runBlocking { fetchWalkingPlaces() }
                walkingPlacesCache.clear()
                walkingPlacesCache.addAll(places)
                Log.d("ItemFromDBViewModel", "Loaded ${places.size} walking places")
            } catch (e: Exception) {
                Log.e("ItemFromDBViewModel", "Error loading walking places: ${e.message}")
            }
        }

        if (foodPlacesCache.isEmpty()) {
            Log.d("ItemFromDBViewModel", "Food places cache is empty, loading places")
            // Загружаем синхронно вместо асинхронной загрузки
            try {
                val places = runBlocking { fetchFoodPlaces() }
                foodPlacesCache.clear()
                foodPlacesCache.addAll(places)
                Log.d("ItemFromDBViewModel", "Loaded ${places.size} food places")
            } catch (e: Exception) {
                Log.e("ItemFromDBViewModel", "Error loading food places: ${e.message}")
            }
        }

        val allPlaces = walkingPlacesCache + foodPlacesCache
        val selectedPlacesIds = _selectedForMap.value
        val selectedPlaces = allPlaces.filter { selectedPlacesIds.contains(it.id) }

        Log.d("ItemFromDBViewModel", "All places: ${allPlaces.size}, Selected places: ${selectedPlaces.size}")
        Log.d("ItemFromDBViewModel", "Selected place IDs: $selectedPlacesIds")
        Log.d("ItemFromDBViewModel", "Selected place names: ${selectedPlaces.map { it.name }}")

        return selectedPlaces
    }
    // Очистить выбор всех мест
    fun clearSelections() {
        _selectedForMap.value = setOf()
        Log.d("ItemFromDBViewModel", "Cleared all selections")
    }

    fun clearCache() {
        walkingPlacesCache.clear()
        foodPlacesCache.clear()
    }
}