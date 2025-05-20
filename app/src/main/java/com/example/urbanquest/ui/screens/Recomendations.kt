package com.example.urbanquest.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.urbanquest.R
import com.example.urbanquest.ui.viewmodel.RecommendationViewModel
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.domain.model.ItemFromDB
import com.example.urbanquest.domain.utils.isOpen

//Composable-функция списка рекомендаций
@Composable
fun Recommendations(
    navController: NavHostController,
    viewModel: RecommendationViewModel,
    itemFromDBViewModel: ItemFromDBViewModel
) {
    val recommendations by viewModel.recommendations.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isError by viewModel.isError.observeAsState(false)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Log.d("Recommendations", "Number of recommendations: ${recommendations.size}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Row(modifier = Modifier.padding(bottom = 8.dp, start = 18.dp)) {
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
                text = stringResource(R.string.LABEL_recomendationList),
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 28.sp
                    screenWidth > 360.dp -> 30.sp
                    else -> 30.sp
                }
            )
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (isError) {
            Text(
                stringResource(R.string.error_warning),
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally))
        } else {
            if (recommendations.isEmpty()) {
                Text(
                    stringResource(R.string.no_result),
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 32.dp),
                    maxLines = 2
                )
            } else {
                recommendations.forEach { item ->
                    RecommendationItem(item, navController, itemFromDBViewModel)
                }
            }
        }
    }
}

@Composable
fun RecommendationItem(item: ItemFromDB, navController: NavHostController, itemFromDBViewModel: ItemFromDBViewModel) {

    val isClicked = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier
        .padding(start = 24.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
        .clip(RoundedCornerShape(15.dp))
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.secondaryContainer)
        .clickable {
            itemFromDBViewModel.selectPlace(item)
            navController.navigate("placeItem")
        }
    ) {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        Row(){

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.imageURL)
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
                            item.name,
                            modifier = Modifier.padding(top = 8.dp),
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
                            .padding(top = 8.dp, end = 16.dp)
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
                    Text(item.address,
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

                Row (
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(imageVector = ImageVector.vectorResource(
                        id = R.drawable.star_icon
                    ),
                        contentDescription = "rate",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(20.dp))
                    Text(item.rate.toString(),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(start = 3.dp),
                        fontSize = when {
                            screenWidth <= 360.dp -> 16.sp
                            screenWidth > 360.dp -> 18.sp
                            else -> 18.sp
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
                    Text(text = isOpen(item.working_time),
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
