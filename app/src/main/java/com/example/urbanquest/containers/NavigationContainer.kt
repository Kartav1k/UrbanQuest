package com.example.urbanquest.containers

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.urbanquest.AuthorizationScreens.Authorization
import com.example.urbanquest.AuthorizationScreens.ChoiceAuthorization
import com.example.urbanquest.AuthorizationScreens.PasswordRecovery
import com.example.urbanquest.AuthorizationScreens.Registration
import com.example.urbanquest.Favourite
import com.example.urbanquest.MenuHub
import com.example.urbanquest.ProfileScreen
import com.example.urbanquest.Search
import com.example.urbanquest.YandexMap

var isAuthorization: Boolean = false
//navController: NavHostController
@Composable
fun NavigationContainer(navController: NavHostController) {

    NavHost(navController = navController,
        startDestination = "Choice_authorization"){
        composable("Choice_authorization"){
            ChoiceAuthorization(navController)
        }
        composable("Registration"){
            Registration(navController, isAuthorization)
        }
        composable("Authorization"){
            Authorization(navController, isAuthorization)
        }
        composable("MenuHub"){
            MenuHub(navController, isAuthorization)
        }
        composable("PasswordRecovery"){
            PasswordRecovery(navController, isAuthorization)
        }
        composable("Profile"){
            ProfileScreen(navController, isAuthorization)
        }
        composable("YandexMap"){
            YandexMap(navController, isAuthorization)
        }
        composable("Search"){
            Search(navController, isAuthorization)
        }
        composable("Favourite"){
            Favourite(navController, isAuthorization)
        }
    }
}