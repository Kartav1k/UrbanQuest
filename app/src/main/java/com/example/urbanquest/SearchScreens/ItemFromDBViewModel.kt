package com.example.urbanquest.SearchScreens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.urbanquest.SearchScreens.data.ItemFromDB

//ViewModel для списка? Не помню


class ItemFromDBViewModel : ViewModel() {

    private val _selectedPlace = MutableLiveData<ItemFromDB?>()
    val selectedPlace: LiveData<ItemFromDB?> = _selectedPlace

    fun selectPlace(place: ItemFromDB) {
        _selectedPlace.value = place
    }
}
