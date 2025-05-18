package com.example.urbanquest.containers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.urbanquest.AuthorizationScreens.Authorization
import com.example.urbanquest.AuthorizationScreens.ChoiceAuthorization
import com.example.urbanquest.AuthorizationScreens.PasswordRecovery
import com.example.urbanquest.AuthorizationScreens.Registration
import com.example.urbanquest.AuthorizationScreens.UserViewModel
import com.example.urbanquest.Favourite
import com.example.urbanquest.FoodPlaces
import com.example.urbanquest.MenuHub
import com.example.urbanquest.ProfileScreens.AchievementsScreen
import com.example.urbanquest.ProfileScreens.InfoAboutApp
import com.example.urbanquest.ProfileScreens.ProfileScreen
import com.example.urbanquest.ProfileScreens.SettingsScreen
import com.example.urbanquest.ProfileScreens.ThemeViewModel
import com.example.urbanquest.RecomendationTest
import com.example.urbanquest.RecommendationViewModel
import com.example.urbanquest.Recommendations
import com.example.urbanquest.SearchScreens.ItemFromDBViewModel
import com.example.urbanquest.SearchScreens.PlaceItem
import com.example.urbanquest.SearchScreens.Search
import com.example.urbanquest.WalkingPlaces
import com.example.urbanquest.YandexMap
import com.google.firebase.auth.FirebaseAuth


@Composable
fun NavigationContainer(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()
    val itemFromDBViewModel: ItemFromDBViewModel = viewModel()
    val recommendationViewModel: RecommendationViewModel = viewModel()

    val isAuthorized by userViewModel.isAuthorized


    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        userViewModel.updateAuthState(currentUser)
    }

    val startDestination = if (isAuthorized) "MenuHub" else "Choice_authorization"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("Choice_authorization"){
            ChoiceAuthorization(navController, userViewModel)
        }
        composable("Registration"){
            Registration(navController, userViewModel)
        }
        composable("Authorization"){
            Authorization(navController, userViewModel)
        }
        composable("PasswordRecovery"){
            PasswordRecovery(navController, userViewModel)
        }
        composable("MenuHub"){
            MenuHub(navController, userViewModel)
        }
        composable("YandexMap"){
            YandexMap(navController, userViewModel)
        }
        composable("Search"){
            Search(navController, userViewModel, itemFromDBViewModel)
        }
        composable("Favourite"){
            Favourite(navController, userViewModel, itemFromDBViewModel)
        }
        composable("Profile"){
            ProfileScreen(navController, userViewModel)
        }
        composable("Settings"){
            SettingsScreen(navController, userViewModel, themeViewModel)
        }
        composable("InfoAboutApp"){
            InfoAboutApp(navController, userViewModel)
        }
        composable("Achievements") {
            AchievementsScreen(navController, userViewModel)
        }
        composable("placeItem") {
            PlaceItem(navController, itemFromDBViewModel)
        }
        composable("RecomendationTest"){
            RecomendationTest(navController, recommendationViewModel)
        }
        composable("RecomendationList"){
            Recommendations(navController, recommendationViewModel, itemFromDBViewModel)
        }
        composable("FoodPlaces"){
            FoodPlaces(navController, itemFromDBViewModel, userViewModel)
        }
        composable("WalkingPlaces"){
            WalkingPlaces(navController, itemFromDBViewModel, userViewModel)
        }
        composable("map/{lat}/{lon}") { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull()
            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull()
            if (lat != null && lon != null) {
                YandexMap(navController, userViewModel, lat, lon)
            }
        }
    }
}