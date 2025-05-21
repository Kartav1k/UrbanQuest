package com.example.urbanquest.core.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.urbanquest.BuildConfig
import com.example.urbanquest.core.navigation.Container
import com.example.urbanquest.ui.theme.UrbanQuestTheme
import com.example.urbanquest.ui.viewmodel.ThemeViewModel
import com.yandex.mapkit.MapKitFactory

class MainActivity : ComponentActivity() {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Log.d("MainActivity", "Location permissions granted")
            }
            else -> {
                Log.d("MainActivity", "Location permissions denied")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запрос разрешений на местоположение
        checkLocationPermissions()

        val mapkitApiKey = BuildConfig.MAPKIT_API_KEY
        MapKitFactory.setApiKey(mapkitApiKey)

        val themeViewModel: ThemeViewModel by viewModels()

        themeViewModel.isDarkTheme.observe(this, Observer { isDarkTheme ->
            setContent {
                UrbanQuestTheme(darkTheme = isDarkTheme) {
                    Box(
                        modifier = Modifier.Companion
                            .fillMaxSize()
                    ) {
                        Container()
                    }
                }
            }
        })
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}