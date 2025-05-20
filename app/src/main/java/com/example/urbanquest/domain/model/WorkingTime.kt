package com.example.urbanquest.domain.model

import com.google.firebase.database.PropertyName

data class WorkingTime(
    @get:PropertyName("isWorking") @set:PropertyName("isWorking")
    var isWorking: Boolean = false,
    var time_open: String = "",
    var time_close: String = ""
)