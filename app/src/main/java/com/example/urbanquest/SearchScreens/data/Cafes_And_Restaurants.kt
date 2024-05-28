package com.example.urbanquest.SearchScreens.data

data class Cafes_And_Restaurants(
    val name: String = "",
    val address: String = "",
    val time_open: String = "",
    val time_close: String = "",
    val rate: Double = 0.0,
    val isWorking: Boolean = false,     //Исключить мб
    val description: String = "",
    val geopoint_latitude: String = "",
    val geopoint_longtitude: String = "",
    val imageURL: String = "",
    val tags: Map<String, String> = emptyMap(),
    val working_time: Map<String, WorkingTime> = emptyMap()
)
