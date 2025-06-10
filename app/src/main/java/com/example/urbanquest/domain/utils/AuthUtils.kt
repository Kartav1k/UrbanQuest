package com.example.urbanquest.domain.utils

import android.util.Patterns

// Различные вспомогательные функции для процесса регистрации и авторизации
fun isLoginValid(login: String): Boolean {
    return login.isNotBlank() && login.any { it.isLetter() } && login.length in 6..16
}

fun isEmailValid(email: String): Boolean {
    return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    val hasLetter = password.any { it.isLetter() }
    val hasDigit = password.any { it.isDigit() }
    return password.length in 6..16 && hasLetter && hasDigit
}