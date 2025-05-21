package com.example.urbanquest.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.ui.components.RecommendationItem
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.ui.viewmodel.RecommendationViewModel
import com.example.urbanquest.ui.viewmodel.UserViewModel

//Composable-функция списка рекомендаций
@Composable
fun Recommendations(
    navController: NavHostController,
    viewModel: RecommendationViewModel,
    itemFromDBViewModel: ItemFromDBViewModel,
    userViewModel: UserViewModel
) {
    val walkingPlaces by viewModel.walkingPlaces.observeAsState(emptyList())
    val foodPlaces by viewModel.foodPlaces.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isError by viewModel.isError.observeAsState(false)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Верхняя панель
        Row(modifier = Modifier.padding(bottom = 8.dp, start = 18.dp)) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow_icon),
                    contentDescription = "Back button",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Text(
                text = stringResource(R.string.LABEL_recomendationList),
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 28.sp
            )
        }

        if (isLoading) {
            // Индикатор загрузки
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (isError) {
            // Обработка ошибки
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.error_warning),
                    color = Color.Red
                )
            }
        } else if (walkingPlaces.isEmpty() && foodPlaces.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.no_result),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (walkingPlaces.isNotEmpty()) {
                    item {
                        Text(
                            text = "Места для прогулок",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    items(walkingPlaces.size) { index ->
                        RecommendationItem(
                            item = walkingPlaces[index],
                            navController = navController,
                            itemFromDBViewModel = itemFromDBViewModel,
                            userViewModel = userViewModel
                        )
                    }
                }

                if (foodPlaces.isNotEmpty()) {
                    item {
                        Text(
                            text = "Заведения",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    items(foodPlaces.size) { index ->
                        RecommendationItem(
                            item = foodPlaces[index],
                            navController = navController,
                            itemFromDBViewModel = itemFromDBViewModel,
                            userViewModel = userViewModel
                        )
                    }
                }
            }
        }
    }
}