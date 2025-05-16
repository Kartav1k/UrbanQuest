package com.example.urbanquest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urbanquest.SearchScreens.data.ItemFromDB
import kotlinx.coroutines.launch

//ViewModel для списка рекомендаций

class RecommendationViewModel : ViewModel() {
    private val _recommendations = MutableLiveData<List<ItemFromDB>>()
    val recommendations: LiveData<List<ItemFromDB>> get() = _recommendations

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    fun fetchRecommendations(selectedTags: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val recommendations = fetchRecommendations2(selectedTags)
                _recommendations.value = recommendations
            } catch (e: Exception) {
                _isError.value = true
                Log.d("Firebase", "Error fetching recommendations: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

