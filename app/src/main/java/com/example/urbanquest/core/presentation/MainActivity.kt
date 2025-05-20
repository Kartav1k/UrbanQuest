package com.example.urbanquest.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import com.example.urbanquest.BuildConfig
import com.example.urbanquest.core.navigation.Container
import com.example.urbanquest.ui.theme.UrbanQuestTheme
import com.example.urbanquest.ui.viewmodel.ThemeViewModel
import com.yandex.mapkit.MapKitFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}