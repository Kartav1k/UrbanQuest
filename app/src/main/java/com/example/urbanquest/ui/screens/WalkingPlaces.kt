package com.example.urbanquest.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.urbanquest.ui.components.SearchItem
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch


@Composable
fun WalkingPlaces(
    navController: NavHostController,
    itemFromDBViewModel: ItemFromDBViewModel,
    userViewModel: UserViewModel
) {
    key(navController.currentDestination?.route) {
        val isLoading by itemFromDBViewModel.isLoadingWalking.observeAsState(initial = false)
        val isLoadingMore by itemFromDBViewModel.isLoadingMoreWalking.observeAsState(initial = false)
        val hasMorePages by itemFromDBViewModel.hasMoreWalkingPages.observeAsState(initial = true)
        val places by itemFromDBViewModel.walkingPlaces.observeAsState(initial = emptyList())

        // Состояние списка и скролла
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        // Показывать ли кнопку "Наверх"
        val showScrollToTopButton by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 5
            }
        }

        // Загружаем первую страницу при первом запуске
        LaunchedEffect(Unit) {
            if (places.isEmpty() && !isLoading) {
                itemFromDBViewModel.loadWalkingPlacesFirstPage()
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp

                // Заголовок
                Row(modifier = Modifier.padding(bottom = 8.dp, start = 20.dp)) {
                    IconButton(
                        onClick = {
                            itemFromDBViewModel.clearWalkingPlacesCache()
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
                    isLoading && places.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    places.isEmpty() && !isLoading -> {
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
                        Column {
                            // Список мест
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.weight(1f)
                            ) {
                                items(places) { place ->
                                    SearchItem(
                                        context = LocalContext.current,
                                        place = place,
                                        navController = navController,
                                        itemFromDBViewModel = itemFromDBViewModel,
                                        userViewModel = userViewModel
                                    )
                                }

                                if (hasMorePages) {
                                    item(key = "load_more_button") {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Button(
                                                onClick = {
                                                    itemFromDBViewModel.loadMoreWalkingPlaces()
                                                },
                                                enabled = !isLoadingMore
                                            ) {
                                                if (isLoadingMore) {
                                                    Row {
                                                        CircularProgressIndicator(
                                                            modifier = Modifier
                                                                .size(16.dp)
                                                                .padding(end = 8.dp),
                                                            strokeWidth = 2.dp,
                                                            color = MaterialTheme.colorScheme.onPrimary
                                                        )
                                                        Text("Загрузка...")
                                                    }
                                                } else {
                                                    Text("Еще")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showScrollToTopButton) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp)
                        .size(40.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Наверх",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}