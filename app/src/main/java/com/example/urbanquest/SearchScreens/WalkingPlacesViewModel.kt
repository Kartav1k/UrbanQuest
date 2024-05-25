package com.example.urbanquest.SearchScreens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.urbanquest.SearchScreens.data.Walking_Place_Item

class WalkingPlaceViewModel : ViewModel() {

    private val _selectedPlace = MutableLiveData<Walking_Place_Item?>()
    val selectedPlace: LiveData<Walking_Place_Item?> = _selectedPlace

    fun selectPlace(place: Walking_Place_Item) {
        _selectedPlace.value = place
    }
}