package com.example.urbanquest.SearchScreens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.urbanquest.R


@Composable
fun PlaceItem(navController: NavHostController, viewModel: ItemFromDBViewModel) {


    val place by viewModel.selectedPlace.observeAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    place?.let { place ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .padding(top = 4.dp, end = 8.dp)
                        .size(18.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow_icon),
                        contentDescription = "Back button",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }

                Text(
                    text = place.name,
                    modifier = Modifier.padding(top = 6.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = when {
                        screenWidth <= 360.dp -> 24.sp
                        screenWidth > 360.dp -> 24.sp
                        else -> 24.sp
                    },
                    overflow = TextOverflow.Ellipsis
                )
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(place.imageURL)
                    .build(),
                contentDescription = "Place Image",
                modifier = Modifier
                    .height(160.dp)
                    .padding(start = 24.dp, end = 24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

            Row(modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){

                Spacer(modifier = Modifier.width(28.dp))

                Box(modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(45.dp)
                    )
                    .clickable {
                        navController.navigate("map/${place.geopoint_latitude}/${place.geopoint_longtitude}")
                    }
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.map_pin_icon),
                            contentDescription = "Map pin icon",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.show_on_map),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.clock_icon),
                        contentDescription = "Clock icon",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        text = isOpen(place.workingTime),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                        contentDescription = "Rate icon",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        text = place.rate.toString(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                Spacer(modifier = Modifier.width(40.dp))
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(place.tags.entries.toList()) { tag ->
                    TagItem(value = tag.value)
                }
            }

            Text(
                text = place.description,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 16.sp
            )
        }
    }
}
