package com.example.urbanquest.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.urbanquest.YandexMap
import com.example.urbanquest.ui.screens.AchievementsScreen
import com.example.urbanquest.ui.screens.Authorization
import com.example.urbanquest.ui.screens.ChoiceAuthorization
import com.example.urbanquest.ui.screens.Favourite
import com.example.urbanquest.ui.screens.FoodPlaces
import com.example.urbanquest.ui.screens.InfoAboutApp
import com.example.urbanquest.ui.screens.MenuHub
import com.example.urbanquest.ui.screens.PasswordRecovery
import com.example.urbanquest.ui.screens.PlaceItem
import com.example.urbanquest.ui.screens.ProfileScreen
import com.example.urbanquest.ui.screens.RecomendationTest
import com.example.urbanquest.ui.screens.Recommendations
import com.example.urbanquest.ui.screens.Registration
import com.example.urbanquest.ui.screens.Search
import com.example.urbanquest.ui.screens.SettingsScreen
import com.example.urbanquest.ui.screens.WalkingPlaces
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.ui.viewmodel.RecommendationViewModel
import com.example.urbanquest.ui.viewmodel.ThemeViewModel
import com.example.urbanquest.ui.viewmodel.UserViewModel
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
            RecomendationTest(navController, recommendationViewModel, userViewModel)
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