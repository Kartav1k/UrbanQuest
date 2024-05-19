package com.example.urbanquest

 data class Walking_Place_Item
     (
      val name: String = "",
      val address: String = "",
      val time_open: String = "",
      val time_close: String = "",
      val rate: Double = 0.0,
      val isWorking: Boolean = false,
      val description: String = "",
      val geopoint_latitude: String = "",
      val geopoint_longtitude: String = "",
      val imageURL: String = ""
 )
