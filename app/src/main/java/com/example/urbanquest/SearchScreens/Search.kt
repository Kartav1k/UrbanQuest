package com.example.urbanquest.SearchScreens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.urbanquest.AuthorizationScreens.UserViewModel
import com.example.urbanquest.R
import com.example.urbanquest.SearchScreens.data.ItemFromDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


var searchListOfWalkingPlaces: ArrayList<ItemFromDB> = arrayListOf()
var searchListOfCafes_And_Restaurants: ArrayList<ItemFromDB> = arrayListOf()

//Composable-функция поиска
@Composable
fun Search(navController: NavHostController, userViewModel: UserViewModel, itemFromDBViewModel: ItemFromDBViewModel){

    var searchRequest by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isError by remember { mutableStateOf(false) }
    var isEmpty by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val context = LocalContext.current

    suspend fun fetchData(query: String) {
        isLoading = true
        try {
            val (walkingPlaces, cafesAndRestaurants) = searchItems(query)
            searchListOfWalkingPlaces.clear()
            searchListOfCafes_And_Restaurants.clear()
            searchListOfWalkingPlaces.addAll(walkingPlaces)
            searchListOfCafes_And_Restaurants.addAll(cafesAndRestaurants)
            isEmpty = searchListOfWalkingPlaces.isEmpty() && searchListOfCafes_And_Restaurants.isEmpty()
        } catch (e: Exception) {
            isError = true
            Log.d("Firebase", "Error fetching data: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    //val searchHistory = getSearchHistory(context).take(3)

    Column() {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp


        Row(modifier = Modifier.padding(bottom = 8.dp, start = 20.dp)) {

            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(top = 4.dp)
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


        TextField(
            value = searchRequest,
            onValueChange = {
                searchRequest = it
                searchJob?.cancel()
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    fetchData(searchRequest)
                }
                //fetchData(searchRequest)
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
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.magnifier_icon),
                    contentDescription = "Search icon",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(25.dp)
                        .padding(end = 4.dp)
                        .clickable {
                            Log.d("Firebase", "Search activated")
                            searchJob?.cancel()
                            searchJob = CoroutineScope(Dispatchers.Main).launch {
                                fetchData(searchRequest)
                            }
                        }
                )
            },
            trailingIcon = {
                if (searchRequest.isNotBlank()) {
                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.clear_icon),
                        contentDescription = "Clear icon",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                searchRequest = ""
                                searchListOfWalkingPlaces.clear()
                                searchListOfCafes_And_Restaurants.clear()
                                isEmpty = false
                                isError = false
                                isLoading = false
                                keyboardController?.hide()
                            }
                    )
                }
            }
        )


        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            when {
                isError -> {
                    key(searchRequest) {
                        Log.d("Firebase", "Error")
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error. Try again.",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                searchJob?.cancel()
                                searchJob = CoroutineScope(Dispatchers.Main).launch {
                                    fetchData(searchRequest)
                                }
                            }) {
                                Text("Refresh")
                            }
                        }
                    }
                }

                isEmpty -> {
                    Log.d("Firebase", "Check to void request - 1")
                    key(searchRequest) {
                        if (searchListOfWalkingPlaces.isEmpty() && searchListOfCafes_And_Restaurants.isEmpty()) {
                            Log.d("Firebase", "Check to void request - 2")
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
                    }
                }

                else -> {
                    Log.d("Firebase", "Normal request - 1")
                    key(searchRequest) {
                        LazyColumn {
                            Log.d("Firebase", "Normal request - 2")
                            items(searchListOfWalkingPlaces + searchListOfCafes_And_Restaurants) { place ->
                                SearchItem(
                                    context = context,
                                    place = place,
                                    navController,
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
}