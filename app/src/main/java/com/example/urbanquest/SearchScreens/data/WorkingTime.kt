package com.example.urbanquest.SearchScreens.data

import com.google.firebase.database.PropertyName

//Data-класс для времени работы места

data class WorkingTime(
    @get:PropertyName("isWorking") @set:PropertyName("isWorking")
    var isWorking: Boolean = false,
    var time_open: String = "",
    var time_close: String = ""
)
