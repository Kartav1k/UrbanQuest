package com.example.urbanquest.SearchScreens.data

data class ItemFromDB
     (
 val name: String = "",
 val address: String = "",
 val timeOpen: String = "",
 val timeClose: String = "",
 val rate: Double = 0.0,
 val description: String = "",
 val geopointLatitude: String = "",
 val geopointLongtitude: String = "",
 val imageURL: String = "",
 val tags: Map<String, String> = emptyMap(),
 val workingTime: Map<String, WorkingTime> = emptyMap(),
 var matchCount: Int = 0
 )