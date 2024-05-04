package com.example.urbanquest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun MenuHub(navController: NavHostController, isAuthorization: Boolean){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween){

        Text(
            text = "Меню",
            fontSize = 32.sp,
            modifier = Modifier.padding(start = 32.dp, top = 18.dp, end = 236.dp, bottom = 26.dp),
            color = MaterialTheme.colorScheme.tertiary
        )



    }
}

