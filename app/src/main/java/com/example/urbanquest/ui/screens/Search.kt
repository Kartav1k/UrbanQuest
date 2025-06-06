package com.example.urbanquest.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.domain.model.ItemFromDB
import com.example.urbanquest.ui.components.SearchItem
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.ui.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


var searchListOfWalkingPlaces: ArrayList<ItemFromDB> = arrayListOf()
var searchListOfCafes_And_Restaurants: ArrayList<ItemFromDB> = arrayListOf()

//Composable-функция поиска
@Composable
fun Search(
    navController: NavHostController,
    userViewModel: UserViewModel,
    itemFromDBViewModel: ItemFromDBViewModel
) {
    var searchRequest by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val context = LocalContext.current

    // Observe search states
    val isLoading by itemFromDBViewModel.isLoadingSearch.observeAsState(initial = false)
    val isLoadingMore by itemFromDBViewModel.isLoadingMoreSearch.observeAsState(initial = false)
    val hasMorePages by itemFromDBViewModel.hasMoreSearchPages.observeAsState(initial = true)
    val searchResults by itemFromDBViewModel.searchResults.observeAsState(initial = emptyList())
    val currentQuery by itemFromDBViewModel.currentSearchQuery.observeAsState(initial = "")

    // Состояние списка и скролла
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        // Header
        Row(modifier = Modifier.padding(bottom = 8.dp, start = 20.dp)) {
            IconButton(
                onClick = {
                    itemFromDBViewModel.clearSearchCache()
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
                text = stringResource(R.string.LABEL_search),
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 32.sp
                    screenWidth > 360.dp -> 36.sp
                    else -> 36.sp
                }
            )
        }

        // Search TextField
        TextField(
            value = searchRequest,
            onValueChange = { newValue ->
                searchRequest = newValue
                searchJob?.cancel()

                if (newValue.isBlank()) {
                    itemFromDBViewModel.clearSearchCache()
                } else {
                    searchJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(500) // Debounce
                        itemFromDBViewModel.searchFirstPage(newValue)
                    }
                }
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search_placeholder),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    fontSize = 14.sp
                )
            },
            shape = RoundedCornerShape(90.dp),
            singleLine = true,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.magnifier_icon),
                    contentDescription = "Search icon",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(25.dp)
                        .padding(end = 4.dp)
                        .clickable {
                            if (searchRequest.isNotBlank()) {
                                searchJob?.cancel()
                                searchJob = CoroutineScope(Dispatchers.Main).launch {
                                    itemFromDBViewModel.searchFirstPage(searchRequest)
                                }
                            }
                        }
                )
            },
            trailingIcon = {
                if (searchRequest.isNotBlank()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.clear_icon),
                        contentDescription = "Clear icon",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                searchRequest = ""
                                itemFromDBViewModel.clearSearchCache()
                                keyboardController?.hide()
                            }
                    )
                }
            }
        )

        // Content based on state
        when {
            isLoading && searchResults.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            searchResults.isEmpty() && currentQuery.isNotBlank() && !isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.item_not_found),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 18.sp
                    )
                }
            }

            searchResults.isNotEmpty() -> {
                Column {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.weight(1f)
                    ) {
                        items(searchResults) { place ->
                            SearchItem(
                                context = context,
                                place = place,
                                navController = navController,
                                itemFromDBViewModel = itemFromDBViewModel,
                                userViewModel = userViewModel
                            )
                        }

                        if (hasMorePages) {
                            item(key = "load_more_search_button") {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(
                                        onClick = {
                                            itemFromDBViewModel.searchMoreResults()
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

            else -> {
                // Empty state - show nothing or search prompt
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Введите запрос для поиска",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}