package com.example.urbanquest.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urbanquest.domain.model.ItemFromDB
import com.example.urbanquest.domain.utils.fetchRecommendations2
import kotlinx.coroutines.launch

//ViewModel для списка рекомендаций

class RecommendationViewModel : ViewModel() {
    private val _walkingPlaces = MutableLiveData<List<ItemFromDB>>()
    val walkingPlaces: LiveData<List<ItemFromDB>> get() = _walkingPlaces

    private val _foodPlaces = MutableLiveData<List<ItemFromDB>>()
    val foodPlaces: LiveData<List<ItemFromDB>> get() = _foodPlaces

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    private val _recommendationLimit = MutableLiveData(10)
    val recommendationLimit: LiveData<Int> get() = _recommendationLimit

    fun setRecommendationLimit(limit: Int) {
        _recommendationLimit.value = limit
    }

    fun fetchRecommendations(selectedTags: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val limit = _recommendationLimit.value ?: 10
                val (walkingPlaces, foodPlaces) = fetchRecommendations2(selectedTags, limit)
                _walkingPlaces.value = walkingPlaces
                _foodPlaces.value = foodPlaces
            } catch (e: Exception) {
                _isError.value = true
                Log.e("RecommendationViewModel", "Error fetching recommendations: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}

