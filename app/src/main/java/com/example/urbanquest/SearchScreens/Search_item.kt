package com.example.urbanquest.SearchScreens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.urbanquest.R
import com.example.urbanquest.SearchScreens.data.ItemFromDB


@Composable
fun SearchItem(context: Context, place: ItemFromDB, navController: NavHostController, itemFromDBViewModel: ItemFromDBViewModel){

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
    val isClicked = remember { mutableStateOf(false) }





    Box(modifier = Modifier
        .padding(start = 24.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
        .clip(RoundedCornerShape(15.dp))
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.secondaryContainer)
        .clickable {
            itemFromDBViewModel.selectPlace(place)
            navController.navigate("placeItem")
        }
    ) {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val time_open = place.time_open

        Row(){

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(place.imageURL)
                    .build(),
                contentDescription = "Icon from Storage",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, end = 4.dp, bottom = 12.dp)
                .size(96.dp),
                placeholder = painterResource(R.drawable.loading),
                error = painterResource(R.drawable.placeholder_icon),
                filterQuality = FilterQuality.High,
                alignment = Alignment.Center,
            )


            Column {

                //The line with the favourite places

                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(place.name,
                        modifier = Modifier
                            .padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = when {
                            screenWidth <= 360.dp -> 20.sp
                            screenWidth > 360.dp -> 26.sp
                            else -> 22.sp
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.favourite_icon),
                        contentDescription = "favourite icon",
                        tint = if (isClicked.value) Color.Red else MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .padding(top = 8.dp, end = 16.dp)
                            .size(25.dp)
                            .clickable {
                                isClicked.value = !isClicked.value
                                /*val editor = sharedPref.edit()
                                if (isClicked.value) {
                                    editor.putBoolean(name, true)
                                    saveSearchQuery(context, name) // Добавление в историю при нажатии на иконку
                                } else {
                                    editor.remove(name)
                                }
                                editor.apply()*/
                            }
                    )
                }


                //The line with the address


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = ImageVector.vectorResource(
                        id = R.drawable.map_pin_icon
                    ),
                        contentDescription = "map_pin",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(20.dp))
                    Text(place.address,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(start = 3.dp),
                        maxLines = 3,
                        softWrap = true,
                        fontSize = when {
                            screenWidth <= 360.dp -> 16.sp
                            screenWidth > 360.dp -> 18.sp
                            else -> 18.sp
                        }
                    )
                }


                //The line with the rate


                Row (
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(imageVector = ImageVector.vectorResource(
                        id = R.drawable.star_icon
                    ),
                        contentDescription = "rate",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(20.dp))
                    Text(place.rate.toString(),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(start = 3.dp),
                        fontSize = when {
                            screenWidth <= 360.dp -> 16.sp
                            screenWidth > 360.dp -> 18.sp
                            else -> 18.sp
                        }
                    )
                }


                //The line with the time


                Row (modifier = Modifier
                    .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = ImageVector.vectorResource(
                        id = R.drawable.clock_icon
                    ),
                        contentDescription = "map_pin",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Text(text = isOpen(place.working_time),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(start = 3.dp),
                        fontSize = when {
                            screenWidth <= 360.dp -> 16.sp
                            screenWidth > 360.dp -> 18.sp
                            else -> 18.sp
                        }
                    )
                }
            }
        }
    }
}