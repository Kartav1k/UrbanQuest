package com.example.urbanquest.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.urbanquest.ui.viewmodel.UserViewModel
import com.example.urbanquest.R
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.ui.components.SearchItem
import kotlinx.coroutines.delay


@Composable
fun WalkingPlaces(navController: NavHostController, itemFromDBViewModel: ItemFromDBViewModel, userViewModel: UserViewModel) {
    key(navController.currentDestination?.route) {
        val isLoading by itemFromDBViewModel.isLoadingWalking.observeAsState(initial = true)
        var places by remember { mutableStateOf(itemFromDBViewModel.getWalkingPlaces()) }
        var isTimeout by remember { mutableStateOf(false) }

        LaunchedEffect(isLoading) {
            if (isLoading) {
                delay(5000)
                if (isLoading) {
                    isTimeout = true
                }
            } else {
                places = itemFromDBViewModel.getWalkingPlaces()
                isTimeout = false
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
                    text = stringResource(R.string.LABEL_walkingplaces),
                    modifier = Modifier.padding(top = 10.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = when {
                        screenWidth <= 360.dp -> 28.sp
                        screenWidth > 360.dp -> 26.sp
                        else -> 26.sp
                    }
                )
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isTimeout) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Загрузка занимает больше времени, чем обычно",
                                    color = MaterialTheme.colorScheme.tertiary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )
                                CircularProgressIndicator()
                            }
                        } else {
                            CircularProgressIndicator()
                        }
                    }
                }

                places.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Нет доступных мест",
                            color = MaterialTheme.colorScheme.tertiary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    LazyColumn {
                        items(places) { place ->
                            SearchItem(
                                context = LocalContext.current,
                                place = place,
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
}
