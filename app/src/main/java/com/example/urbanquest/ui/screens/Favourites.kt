package com.example.urbanquest.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.domain.model.ItemFromDB
import com.example.urbanquest.domain.utils.loadFavoritePlaces
import com.example.urbanquest.ui.components.FavoriteItem
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun Favourite(navController: NavHostController, userViewModel: UserViewModel, itemFromDBViewModel: ItemFromDBViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var walkingPlaces by remember { mutableStateOf<List<ItemFromDB>>(emptyList()) }
    var foodPlaces by remember { mutableStateOf<List<ItemFromDB>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (userViewModel.isAuthorized.value) {
            loadFavoritePlaces(
                userViewModel = userViewModel,
                onLoadingChange = { isLoading = it },
                onErrorChange = { error = it },
                onWalkingPlacesLoaded = { walkingPlaces = it },
                onFoodPlacesLoaded = { foodPlaces = it }
            )
        } else {
            isLoading = false
        }
    }

    Column {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        Row(modifier = Modifier.padding(bottom = 8.dp, start = 20.dp)) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow_icon),
                    contentDescription = "Back button",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Text(
                text = stringResource(R.string.LABEL_favourite),
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 32.sp
                    screenWidth > 360.dp -> 36.sp
                    else -> 36.sp
                }
            )
        }

        if (!userViewModel.isAuthorized.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Войдите, чтобы получить доступ к избранному",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate("Choice_authorization")
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(45.dp)
                    ) {
                        Text(
                            "Войти",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        } else if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        error!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                loadFavoritePlaces(
                                    userViewModel = userViewModel,
                                    onLoadingChange = { isLoading = it },
                                    onErrorChange = { error = it },
                                    onWalkingPlacesLoaded = { walkingPlaces = it },
                                    onFoodPlacesLoaded = { foodPlaces = it }
                                )
                            }
                        }
                    ) {
                        Text("Попробовать снова")
                    }
                }
            }
        } else if (walkingPlaces.isEmpty() && foodPlaces.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "У вас пока нет избранных мест",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn {
                if (walkingPlaces.isNotEmpty()) {
                    item {
                        Text(
                            "Места для прогулок",
                            modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 8.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 20.sp
                        )
                    }

                    items(walkingPlaces.size) { index ->
                        val place = walkingPlaces[index]
                        FavoriteItem(
                            context = context,
                            place = place,
                            navController = navController,
                            itemFromDBViewModel = itemFromDBViewModel,
                            userViewModel = userViewModel,
                            onRemoveFromFavorites = { placeId ->
                                walkingPlaces = walkingPlaces.filter { it.id.toString() != placeId }
                            }
                        )
                    }
                }

                if (foodPlaces.isNotEmpty()) {
                    item {
                        Text(
                            "Кафе и рестораны",
                            modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 8.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 20.sp
                        )
                    }

                    items(foodPlaces.size) { index ->
                        val place = foodPlaces[index]
                        FavoriteItem(
                            context = context,
                            place = place,
                            navController = navController,
                            itemFromDBViewModel = itemFromDBViewModel,
                            userViewModel = userViewModel,
                            onRemoveFromFavorites = { placeId ->
                                foodPlaces = foodPlaces.filter { it.id.toString() != placeId }
                            }
                        )
                    }
                }
            }
        }
    }
}