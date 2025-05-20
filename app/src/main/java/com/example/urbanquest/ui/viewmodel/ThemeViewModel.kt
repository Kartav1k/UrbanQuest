package com.example.urbanquest.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ThemeViewModel (application: Application) : AndroidViewModel(application){

    private val _isDarkTheme = MutableLiveData<Boolean>()

    val isDarkTheme: LiveData<Boolean>
        get() = _isDarkTheme

    init {
        loadThemeState()
    }

    fun toggleTheme(isDarkTheme: Boolean) {
        _isDarkTheme.value = isDarkTheme
        setThemeState(getApplication(), isDarkTheme)
    }

    private fun setThemeState(context: Context, isDarkTheme: Boolean){
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences("theme_state", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("state", isDarkTheme).apply()
        }
    }

    private fun loadThemeState() {
        viewModelScope.launch{
            val sharedPreferences = getApplication<Application>().getSharedPreferences("theme_state", Context.MODE_PRIVATE)
            _isDarkTheme.value = sharedPreferences.getBoolean("state", false)
        }
    }
}