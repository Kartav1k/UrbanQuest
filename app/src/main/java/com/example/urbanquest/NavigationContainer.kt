package com.example.urbanquest

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

var isAuthorization: Boolean = false
//navController: NavHostController
@Composable
fun NavigationContainer() {

    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = "choice_authorization"){
        composable("choice_authorization"){
            ChoiceAuthorization(navController)
        }
        composable("Registration"){
            Registration(navController, isAuthorization)
        }
        composable("Authorization"){
            Authorization(navController)
        }
        composable("MenuHub"){
            MenuHub(navController)
        }
    }

}