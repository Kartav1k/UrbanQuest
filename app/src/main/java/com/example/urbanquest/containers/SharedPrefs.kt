package com.example.urbanquest.containers

import android.content.Context



fun setThemeState(context: Context, isDarkTheme: Boolean){
    val sharedPreferences = context.getSharedPreferences("theme_state", Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean("state",isDarkTheme).apply()
}

fun getThemeState(context: Context): Boolean{
    val sharedPreferences = context.getSharedPreferences("theme_state", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("state", false)
}




fun saveSearchQuery(context: Context, query: String) {
    val sharedPref = context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
    val history = getSearchHistory(context).toMutableList()
    if (history.contains(query)) {
        history.remove(query)
    }
    history.add(0, query)
    if (history.size > 3) {
        history.removeAt(history.size - 1)
    }
    sharedPref.edit().putStringSet("history", history.toSet()).apply()
}

fun getSearchHistory(context: Context): List<String> {
    val sharedPref = context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
    return sharedPref.getStringSet("history", emptySet())?.toList() ?: emptyList()
}

fun clearSearchHistory(context: Context) {
    val sharedPref = context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
    sharedPref.edit().clear().apply()
}