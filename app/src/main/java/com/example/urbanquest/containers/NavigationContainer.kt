package com.example.urbanquest.containers

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.urbanquest.AuthorizationScreens.Authorization
import com.example.urbanquest.AuthorizationScreens.ChoiceAuthorization
import com.example.urbanquest.AuthorizationScreens.PasswordRecovery
import com.example.urbanquest.AuthorizationScreens.Registration
import com.example.urbanquest.Favourite
import com.example.urbanquest.FoodPlaces
import com.example.urbanquest.MenuHub
import com.example.urbanquest.ProfileScreens.AchievementsScreen
import com.example.urbanquest.ProfileScreens.FriendList
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


var isAuthorization: Boolean = false

@Composable
fun NavigationContainer(navController: NavHostController) {

    val themeViewModel: ThemeViewModel = viewModel()
    val walkingPlacesViewModel: ItemFromDBViewModel = viewModel()
    val recommendationViewModel: RecommendationViewModel = viewModel()
    val tags: List<String> = listOf()

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
        composable("PasswordRecovery"){
            PasswordRecovery(navController, isAuthorization)
        }
        composable("MenuHub"){
            MenuHub(navController, isAuthorization)
        }
        composable("YandexMap"){
            YandexMap(navController, isAuthorization)
        }
        composable("Search"){
            Search(navController, isAuthorization, walkingPlacesViewModel)
        }
        composable("Favourite"){
            Favourite(navController, isAuthorization)
        }
        composable("Profile"){
            ProfileScreen(navController, isAuthorization)
        }
        composable("Settings"){
            SettingsScreen(navController, isAuthorization, themeViewModel)
        }
        composable("InfoAboutApp"){
            InfoAboutApp(navController, isAuthorization)
        }
        composable("Achievements"){
            AchievementsScreen(navController, isAuthorization)
        }
        composable("FriendList"){
            FriendList(navController, isAuthorization)
        }
        composable("placeItem") {
            PlaceItem(navController, walkingPlacesViewModel)
        }
        composable("RecomendationTest"){
            RecomendationTest(navController, recommendationViewModel)
        }
        composable("RecomendationList"){
            Recommendations(navController, recommendationViewModel,walkingPlacesViewModel)
        }
        composable("FoodPlaces"){
            FoodPlaces(navController, isAuthorization, walkingPlacesViewModel)
        }
        composable("WalkingPlaces"){
            WalkingPlaces(navController, isAuthorization, walkingPlacesViewModel)
        }
        composable("map/{lat}/{lon}") { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull()
            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull()
            if (lat != null && lon != null) {
                YandexMap(navController, isAuthorization, lat, lon)
            }
        }
    }
}