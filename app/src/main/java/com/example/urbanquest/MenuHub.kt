package com.example.urbanquest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.ui.theme.BottomAppBarColor
import com.example.urbanquest.ui.theme.White


@Composable
fun MenuHub(navController: NavHostController, isAuthorization: Boolean){
    Box(
        modifier = Modifier
            .fillMaxSize()){

        Text(
            text = "Меню",
            fontSize = 32.sp,
            modifier = Modifier.padding(start = 32.dp, top = 18.dp, end = 236.dp, bottom = 26.dp),
            color = MaterialTheme.colorScheme.tertiary
        )
        BottomAppBar(
            containerColor = BottomAppBarColor,
            contentColor = White,
            contentPadding = PaddingValues(118.dp),

        ) {
            IconButton(onClick = { navController.navigate("MenuHub")}) {
                Icon(Icons.Filled.Home, contentDescription = "Меню")
            }
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Search, contentDescription = "Поиск")
            }
            IconButton(onClick = { }) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Карта") //пока заглушка
            }
            IconButton(onClick = { }) {
                Icon(Icons.Filled.FavoriteBorder, contentDescription = "Избранное")
            }
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Person, contentDescription = "Профиль")
            }
        }
    }
}