package com.example.urbanquest.containers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.urbanquest.ui.theme.UrbanQuestTheme
import com.yandex.mapkit.MapKitFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("205bcfaf-ef8a-46a8-93d0-3115668a4bd0\n")
        setContent {
            UrbanQuestTheme {
                Box(modifier = Modifier
                    .fillMaxSize()
                ){
                    Container()
                }
            }
        }
    }
}



