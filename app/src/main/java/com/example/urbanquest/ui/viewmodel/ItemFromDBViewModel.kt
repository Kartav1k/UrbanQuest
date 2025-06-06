package com.example.urbanquest.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urbanquest.domain.model.ItemFromDB
import com.example.urbanquest.domain.utils.fetchFoodPlaces
import com.example.urbanquest.domain.utils.fetchFoodPlacesPaginated
import com.example.urbanquest.domain.utils.fetchWalkingPlaces
import com.example.urbanquest.domain.utils.fetchWalkingPlacesPaginated
import com.example.urbanquest.domain.utils.searchItemsPaginated
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ItemFromDBViewModel : ViewModel() {
    private val _selectedPlace = MutableLiveData<ItemFromDB?>()
    val selectedPlace: LiveData<ItemFromDB?> = _selectedPlace

    private val walkingPlacesCache = mutableListOf<ItemFromDB>()
    private val foodPlacesCache = mutableListOf<ItemFromDB>()

    private val _isLoadingWalking = MutableLiveData(false)
    val isLoadingWalking: LiveData<Boolean> = _isLoadingWalking

    private val _isLoadingFood = MutableLiveData(false)
    val isLoadingFood: LiveData<Boolean> = _isLoadingFood

    private val _isLoadingMoreWalking = MutableLiveData(false)
    val isLoadingMoreWalking: LiveData<Boolean> = _isLoadingMoreWalking

    private val _isLoadingMoreFood = MutableLiveData(false)
    val isLoadingMoreFood: LiveData<Boolean> = _isLoadingMoreFood

    private val _hasMoreWalkingPages = MutableLiveData(true)
    val hasMoreWalkingPages: LiveData<Boolean> = _hasMoreWalkingPages

    private val _hasMoreFoodPages = MutableLiveData(true)
    val hasMoreFoodPages: LiveData<Boolean> = _hasMoreFoodPages

    private val _foodPlaces = MutableLiveData<List<ItemFromDB>>(emptyList())
    val foodPlaces: LiveData<List<ItemFromDB>> = _foodPlaces

    private val _walkingPlaces = MutableLiveData<List<ItemFromDB>>(emptyList())
    val walkingPlaces: LiveData<List<ItemFromDB>> = _walkingPlaces

    private val searchWalkingPlacesCache = mutableListOf<ItemFromDB>()
    private val searchFoodPlacesCache = mutableListOf<ItemFromDB>()

    private val _isLoadingSearch = MutableLiveData(false)
    val isLoadingSearch: LiveData<Boolean> = _isLoadingSearch

    private val _isLoadingMoreSearch = MutableLiveData(false)
    val isLoadingMoreSearch: LiveData<Boolean> = _isLoadingMoreSearch

    private val _hasMoreSearchPages = MutableLiveData(true)
    val hasMoreSearchPages: LiveData<Boolean> = _hasMoreSearchPages

    private val _searchResults = MutableLiveData<List<ItemFromDB>>(emptyList())
    val searchResults: LiveData<List<ItemFromDB>> = _searchResults

    private val _currentSearchQuery = MutableLiveData<String>("")
    val currentSearchQuery: LiveData<String> = _currentSearchQuery

    // Ключи для пагинации
    private var lastWalkingKey: String? = null
    private var lastFoodKey: String? = null

    private var lastSearchWalkingKey: String? = null
    private var lastSearchFoodKey: String? = null

    private var searchJob: Job? = null

    private val _selectedForMap = MutableStateFlow<Set<Long>>(setOf())
    val selectedForMap: StateFlow<Set<Long>> = _selectedForMap

    fun selectPlace(place: ItemFromDB) {
        _selectedPlace.value = place
    }

    fun loadWalkingPlacesFirstPage() {
        if (_isLoadingWalking.value == true) return

        _isLoadingWalking.value = true
        walkingPlacesCache.clear()
        lastWalkingKey = null
        _hasMoreWalkingPages.value = true

        viewModelScope.launch {
            try {
                val (places, newLastKey) = fetchWalkingPlacesPaginated(null, 10)
                walkingPlacesCache.addAll(places)
                lastWalkingKey = newLastKey
                _hasMoreWalkingPages.value = places.size == 10 && newLastKey != null
                _walkingPlaces.value = walkingPlacesCache.toList() // ДОБАВИТЬ
                _isLoadingWalking.value = false
            } catch (e: Exception) {
                Log.e("ItemFromDBViewModel", "Error loading walking places first page: ${e.message}")
                _isLoadingWalking.value = false
                _hasMoreWalkingPages.value = false
            }
        }
    }

    fun loadMoreWalkingPlaces() {
        if (_isLoadingMoreWalking.value == true || _hasMoreWalkingPages.value == false) return

        _isLoadingMoreWalking.value = true

        viewModelScope.launch {
            try {
                val (places, newLastKey) = fetchWalkingPlacesPaginated(lastWalkingKey, 10)
                if (places.isNotEmpty()) {
                    walkingPlacesCache.addAll(places)
                    lastWalkingKey = newLastKey
                    _hasMoreWalkingPages.value = places.size == 10 && newLastKey != null
                    _walkingPlaces.value = walkingPlacesCache.toList() // ДОБАВИТЬ
                } else {
                    _hasMoreWalkingPages.value = false
                }
                _isLoadingMoreWalking.value = false
            } catch (e: Exception) {
                Log.e("ItemFromDBViewModel", "Error loading more walking places: ${e.message}")
                _isLoadingMoreWalking.value = false
                _hasMoreWalkingPages.value = false
            }
        }
    }

    // НОВАЯ функция для загрузки первой страницы еды
    fun loadFoodPlacesFirstPage() {
        if (_isLoadingFood.value == true) return

        _isLoadingFood.value = true
        foodPlacesCache.clear()
        lastFoodKey = null
        _hasMoreFoodPages.value = true

        viewModelScope.launch {
            try {
                val (places, newLastKey) = fetchFoodPlacesPaginated(null, 10)
                foodPlacesCache.addAll(places)
                lastFoodKey = newLastKey
                _hasMoreFoodPages.value = places.size == 10 && newLastKey != null

                // ДОБАВЬТЕ ЭТУ СТРОКУ:
                _foodPlaces.value = foodPlacesCache.toList()

                _isLoadingFood.value = false
            } catch (e: Exception) {
                Log.e("ItemFromDBViewModel", "Error loading food places first page: ${e.message}")
                _isLoadingFood.value = false
                _hasMoreFoodPages.value = false
            }
        }
    }


    fun loadMoreFoodPlaces() {
        if (_isLoadingMoreFood.value == true || _hasMoreFoodPages.value == false) return

        _isLoadingMoreFood.value = true

        viewModelScope.launch {
            try {
                val (places, newLastKey) = fetchFoodPlacesPaginated(lastFoodKey, 10)
                if (places.isNotEmpty()) {
                    foodPlacesCache.addAll(places)
                    lastFoodKey = newLastKey
                    _hasMoreFoodPages.value = places.size == 10 && newLastKey != null
                    _foodPlaces.value = foodPlacesCache.toList()

                } else {
                    _hasMoreFoodPages.value = false
                }
                _isLoadingMoreFood.value = false
            } catch (e: Exception) {
                Log.e("ItemFromDBViewModel", "Error loading more food places: ${e.message}")
                _isLoadingMoreFood.value = false
                _hasMoreFoodPages.value = false
            }
        }
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

    fun getCurrentWalkingPlaces(): List<ItemFromDB> = walkingPlacesCache.toList()
    fun getCurrentFoodPlaces(): List<ItemFromDB> = foodPlacesCache.toList()

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

    fun searchFirstPage(query: String) {
        if (_isLoadingSearch.value == true || query.isBlank()) return

        _isLoadingSearch.value = true
        _currentSearchQuery.value = query
        searchWalkingPlacesCache.clear()
        searchFoodPlacesCache.clear()
        lastSearchWalkingKey = null
        lastSearchFoodKey = null
        _hasMoreSearchPages.value = true

        viewModelScope.launch {
            try {
                val (walkingPlaces, foodPlaces, keys) = searchItemsPaginated(query, null, null, 10)
                searchWalkingPlacesCache.addAll(walkingPlaces)
                searchFoodPlacesCache.addAll(foodPlaces)

                lastSearchWalkingKey = keys.first
                lastSearchFoodKey = keys.second

                val allResults = searchWalkingPlacesCache + searchFoodPlacesCache
                _searchResults.value = allResults

                // Если получили меньше чем ожидали, значит больше данных нет
                _hasMoreSearchPages.value = walkingPlaces.size == 10 || foodPlaces.size == 10

                _isLoadingSearch.value = false
            } catch (e: Exception) {
                Log.e("ItemFromDBViewModel", "Error searching first page: ${e.message}")
                _isLoadingSearch.value = false
                _hasMoreSearchPages.value = false
            }
        }
    }

    fun searchMoreResults() {
        val query = _currentSearchQuery.value ?: return
        if (_isLoadingMoreSearch.value == true || _hasMoreSearchPages.value == false || query.isBlank()) return

        _isLoadingMoreSearch.value = true

        viewModelScope.launch {
            try {
                val (walkingPlaces, foodPlaces, keys) = searchItemsPaginated(
                    query,
                    lastSearchWalkingKey,
                    lastSearchFoodKey,
                    10
                )

                if (walkingPlaces.isNotEmpty() || foodPlaces.isNotEmpty()) {
                    searchWalkingPlacesCache.addAll(walkingPlaces)
                    searchFoodPlacesCache.addAll(foodPlaces)

                    lastSearchWalkingKey = keys.first
                    lastSearchFoodKey = keys.second

                    val allResults = searchWalkingPlacesCache + searchFoodPlacesCache
                    _searchResults.value = allResults

                    _hasMoreSearchPages.value = walkingPlaces.size == 10 || foodPlaces.size == 10
                } else {
                    _hasMoreSearchPages.value = false
                }

                _isLoadingMoreSearch.value = false
            } catch (e: Exception) {
                Log.e("ItemFromDBViewModel", "Error loading more search results: ${e.message}")
                _isLoadingMoreSearch.value = false
                _hasMoreSearchPages.value = false
            }
        }
    }

    // Очистить выбор всех мест
    fun clearSelections() {
        _selectedForMap.value = setOf()
        Log.d("ItemFromDBViewModel", "Cleared all selections")
    }

    fun clearWalkingPlacesCache() {
        walkingPlacesCache.clear()
        lastWalkingKey = null
        _hasMoreWalkingPages.value = true
        _walkingPlaces.value = emptyList()
        _isLoadingWalking.value = false
        _isLoadingMoreWalking.value = false
        Log.d("ItemFromDBViewModel", "Walking places cache cleared")
    }

    // Очистка кэша для еды
    fun clearFoodPlacesCache() {
        foodPlacesCache.clear()
        lastFoodKey = null
        _hasMoreFoodPages.value = true
        _foodPlaces.value = emptyList()
        _isLoadingFood.value = false
        _isLoadingMoreFood.value = false
        Log.d("ItemFromDBViewModel", "Food places cache cleared")
    }

    // Очистка кэша поиска
    fun clearSearchCache() {
        searchWalkingPlacesCache.clear()
        searchFoodPlacesCache.clear()
        lastSearchWalkingKey = null
        lastSearchFoodKey = null
        _hasMoreSearchPages.value = true
        _searchResults.value = emptyList()
        _currentSearchQuery.value = ""
        _isLoadingSearch.value = false
        _isLoadingMoreSearch.value = false
        Log.d("ItemFromDBViewModel", "Search cache cleared")
    }

    fun clearAllCache() {
        clearWalkingPlacesCache()
        clearFoodPlacesCache()
        clearSearchCache()
        Log.d("ItemFromDBViewModel", "All cache cleared")
    }

    fun searchWithDebounce(query: String) {
        searchJob?.cancel() // Отменяем предыдущий поиск
        searchJob = viewModelScope.launch {
            delay(500)
            searchFirstPage(query)
        }
    }

    override fun onCleared() {
        super.onCleared()

        searchJob?.cancel()
        walkingPlacesCache.clear()
        foodPlacesCache.clear()
        searchWalkingPlacesCache.clear()
        searchFoodPlacesCache.clear()
        _selectedForMap.value = setOf()
        Log.d("ItemFromDBViewModel", "ViewModel cleared - resources released")
    }

}