package com.example.urbanquest.SearchScreens.data

//Data-класс для времени работы места

data class WorkingTime(
    val isWorking: Boolean = false,
    val time_open: String = "",
    val time_close: String = ""
)
