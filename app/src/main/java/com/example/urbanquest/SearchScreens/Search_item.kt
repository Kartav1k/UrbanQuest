package com.example.urbanquest.SearchScreens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import com.example.urbanquest.constants.eightPad
import com.example.urbanquest.constants.fourPad
import com.example.urbanquest.constants.fourteenFontSize
import com.example.urbanquest.constants.sixteenFontSize
import com.example.urbanquest.constants.sixteenPad
import com.example.urbanquest.constants.twelvePad
import com.example.urbanquest.constants.twentyFourPad


//Composable-функция для отображения элемента списка в поиске
@Composable
fun SearchItem(context: Context, place: ItemFromDB, navController: NavHostController, itemFromDBViewModel: ItemFromDBViewModel){

    val isClicked = remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .padding(start = twentyFourPad, end = sixteenPad, top = twentyFourPad, bottom = eightPad)
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

        Row(){

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(place.imageURL)
                    .build(),
                contentDescription = "Icon from Storage",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                .padding(start = eightPad, top = twelvePad, end = fourPad, bottom = twelvePad)
                .size(96.dp),
                placeholder = painterResource(R.drawable.loading),
                error = painterResource(R.drawable.placeholder_icon),
                filterQuality = FilterQuality.High,
                alignment = Alignment.Center,
            )


            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            place.name,
                            modifier = Modifier.padding(top = eightPad),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = when {
                                screenWidth <= 360.dp -> 20.sp
                                screenWidth > 360.dp -> 26.sp
                                else -> 22.sp
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.favourite_icon),
                        contentDescription = "favourite icon",
                        tint = if (isClicked.value) Color.Red else MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .padding(top = eightPad, end = sixteenPad)
                            .size(25.dp)
                            .clickable {
                                isClicked.value = !isClicked.value
                            }
                    )
                }

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
                            screenWidth <= 360.dp -> fourteenFontSize
                            screenWidth > 360.dp -> sixteenFontSize
                            else -> fourteenFontSize
                        }
                    )
                }

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
                        modifier = Modifier.padding(start = 3.dp, top = 4.dp),
                        fontSize = when {
                            screenWidth <= 360.dp -> fourteenFontSize
                            screenWidth > 360.dp -> sixteenFontSize
                            else -> fourteenFontSize
                        }
                    )
                }

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
                            screenWidth <= 360.dp -> 14.sp
                            screenWidth > 360.dp -> 16.sp
                            else -> 14.sp
                        }
                    )
                }
            }
        }
    }
}