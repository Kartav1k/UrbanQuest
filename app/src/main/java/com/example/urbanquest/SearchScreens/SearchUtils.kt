package com.example.urbanquest.SearchScreens

import com.example.urbanquest.SearchScreens.data.WorkingTime
import com.example.urbanquest.constants.close_place
import com.example.urbanquest.constants.open_place
import com.example.urbanquest.constants.unlimited_access
import java.time.LocalDate
import java.time.LocalTime

fun isOpen(working: Map<String, WorkingTime>): String {
    val currentDay = LocalDate.now().dayOfWeek.toString().lowercase()
    val currentTime = LocalTime.now()
    val workingTime = working[currentDay] ?: return close_place
    if (workingTime.time_open.isEmpty() || workingTime.time_close.isEmpty()) {
        return close_place
    }
    if (workingTime.time_open == "Без ограничений" || workingTime.time_close == "Без ограничений") {
        return unlimited_access
    }
    val openTime = LocalTime.parse(workingTime.time_open)
    val closeTime = LocalTime.parse(workingTime.time_close)
    if(currentTime.isAfter(openTime) && currentTime.isBefore(closeTime)){
        return open_place + workingTime.time_open
    }else{
        return close_place
    }
}