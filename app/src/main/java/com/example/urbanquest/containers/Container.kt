package com.example.urbanquest.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.urbanquest.R
import com.example.urbanquest.ui.theme.BottomAppBarColor


@Composable
fun Container(){

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold (
        bottomBar = {
            if (navBackStackEntry?.destination?.route !in listOf(
                    "Choice_authorization",
                    "Registration",
                    "Authorization",
                    "PasswordRecovery"
                )
            ) {
                BottomAppBar(
                    containerColor = BottomAppBarColor,
                    modifier = Modifier.height(56.dp)

                ) {

                    Spacer(Modifier.weight(1f, true))

                    IconButton(
                        onClick = {
                            if (currentRoute != "MenuHub") {
                                navController.navigate("MenuHub")
                            }
                        },
                        enabled = currentRoute != "Menuhub",
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.home_icon),
                            contentDescription = "Menu",
                            Modifier.size(30.dp),
                            tint = if (currentRoute == "MenuHub") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                        )
                    }

                    Spacer(Modifier.weight(1f, true))

                    IconButton(
                        onClick = {
                            if (currentRoute != "Search") {
                                navController.navigate("Search")
                            }
                        },
                        enabled = currentRoute != "Search"
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.magnifier_icon),
                            contentDescription = "Search",
                            Modifier.size(30.dp),
                            tint = if (currentRoute == "Search") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                        )
                    }

                    Spacer(Modifier.weight(1f, true))

                    IconButton(
                        onClick = {
                            if (currentRoute != "YandexMap") {
                                navController.navigate("YandexMap")
                            }
                        },
                        enabled = currentRoute != "YandexMap"
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.map_icon),
                            contentDescription = "Yandex Map",
                            modifier = Modifier
                                .size(40.dp),
                            tint = if (currentRoute == "YandexMap") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                        )
                    }

                    Spacer(Modifier.weight(1f, true))

                    IconButton(
                        onClick = {
                            if (currentRoute != "Favourite") {
                                navController.navigate("Favourite")
                            }
                        },
                        enabled = currentRoute != "Favourite"
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.favourite_icon),
                            contentDescription = "Favourite",
                            Modifier.size(40.dp),
                            tint = if (currentRoute == "Favourite") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                        )
                    }

                    Spacer(Modifier.weight(1f, true))

                    IconButton(
                        onClick = {
                            if (currentRoute != "Profile") {
                                navController.navigate("Profile")
                            }
                        },
                        enabled = currentRoute != "Profile"
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.profile_icon),
                            contentDescription = "Profile",
                            Modifier.size(40.dp),
                            tint = if (currentRoute == "Profile") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                        )
                    }

                    Spacer(Modifier.weight(1f, true))

                }
            }
        }

    ){ innerPadding->
        Box(modifier = Modifier.padding(innerPadding)){
            NavigationContainer(navController)
        }
    }
}