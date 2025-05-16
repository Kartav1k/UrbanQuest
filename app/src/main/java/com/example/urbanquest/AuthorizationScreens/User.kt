package com.example.urbanquest.AuthorizationScreens

data class User(
    val userId: String = "",
    val email: String = "",
    val login: String = "",
    val favourites: Map<String, Boolean> = emptyMap(),
    val achievements: Map<String, Boolean> = emptyMap()
)