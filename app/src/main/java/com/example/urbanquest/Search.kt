package com.example.urbanquest

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.constants.LABEL_search
import com.example.urbanquest.constants.search_placeholder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


var searchListOfWalkingPlaces: ArrayList<Walking_Place_Item> = arrayListOf()
private lateinit var firebaseRef: DatabaseReference

@Composable
fun Search(navController: NavHostController, isAuthorization: Boolean){

    var searchRequest by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isError by remember { mutableStateOf(false) }
    var isEmpty by remember { mutableStateOf(false) }



    fun fetchData(query: String){
        firebaseRef = FirebaseDatabase.getInstance("https://urbanquest-ce793-default-rtdb.europe-west1.firebasedatabase.app/").getReference("walking_places_info")
        firebaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Firebase", "Start 1")
                isEmpty = true
                isError = false
                searchListOfWalkingPlaces.clear()
                if (snapshot.exists()){
                    for (items in snapshot.children){
                        val item = items.getValue(Walking_Place_Item::class.java)
                        if (item != null && item.name?.contains(query, ignoreCase = true) == true){
                            searchListOfWalkingPlaces.add(item)
                            Log.d("Firebase", "Сохранило?")
                        }
                    }
                    isEmpty = searchListOfWalkingPlaces.isEmpty()
                    Log.d("Firebase", if (isEmpty) "Query is empty" else "Query has results")
                }
                else{
                    isError = true
                    Log.d("Firebase", "Impossible error")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "$error")
            }
        })
    }

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
                    imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow),
                    contentDescription = "Back button",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Text(
                text = LABEL_search,
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
                searchRequest=it
                fetchData(searchRequest)
            },
            placeholder = {
                Text(
                    text = search_placeholder,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    fontSize = 14.sp)
            },
            shape = RoundedCornerShape(90.dp),
            singleLine = true,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
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
                Icon(Icons.Filled.Search,
                    contentDescription = "Search icon",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable {
                            Log.d("Firebase", "Нажали поиск")
                            fetchData(searchRequest)
                        }
                )
            },
            trailingIcon = {
                if (searchRequest.isNotBlank()){
                    Icon(Icons.Filled.Clear,
                        contentDescription = "Clear icon",
                        tint=MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.clickable {
                            searchRequest = ""
                            searchListOfWalkingPlaces.clear()
                            isEmpty = false
                            isError = false
                            keyboardController?.hide()
                        }
                    )
                }
            }
        )
        when {
            isError -> {
                key(searchRequest) {
                    Log.d("Firebase", "Срабатывание ошибки")
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ошибка. Попробуйте еще раз.",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            fetchData(searchRequest)
                        }) {
                            Text("Обновить")
                        }
                    }
                }
            }

            isEmpty -> {
                Log.d("Firebase", "Проверка на пустоту 1")
                key(searchRequest) {
                    if (searchListOfWalkingPlaces.isEmpty()) {
                        Log.d("Firebase", "Проверка на пустоту 2")
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Не найдено",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

            else -> {
                Log.d("Firebase", "Все норм 1")
                key(searchRequest) {
                    LazyColumn {
                        Log.d("Firebase", "Все норм 2")
                        items(searchListOfWalkingPlaces) { place ->
                            SearchItem(
                                name = place.name,
                                address = place.address,
                                time_open = place.time_open,
                                rate = place.rate,
                                isWorking = place.isWorking,
                                imageURL = place.imageURL
                            )
                        }
                    }
                }
            }
        }
    }
}
