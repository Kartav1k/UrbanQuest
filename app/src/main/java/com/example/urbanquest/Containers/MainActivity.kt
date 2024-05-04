package com.example.urbanquest.Containers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.urbanquest.Containers.Container
import com.example.urbanquest.ui.theme.UrbanQuestTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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



