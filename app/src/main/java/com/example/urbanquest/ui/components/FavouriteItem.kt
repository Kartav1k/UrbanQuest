package com.example.urbanquest.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.urbanquest.R
import com.example.urbanquest.domain.model.ItemFromDB
import com.example.urbanquest.domain.utils.constants.eightPad
import com.example.urbanquest.domain.utils.constants.fourPad
import com.example.urbanquest.domain.utils.constants.fourteenFontSize
import com.example.urbanquest.domain.utils.constants.sixteenPad
import com.example.urbanquest.domain.utils.constants.twelvePad
import com.example.urbanquest.domain.utils.isOpen
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.ui.viewmodel.UserViewModel

@Composable
fun FavoriteItem(
    context: Context,
    place: ItemFromDB,
    navController: NavHostController,
    itemFromDBViewModel: ItemFromDBViewModel,
    userViewModel: UserViewModel,
    onRemoveFromFavorites: (String) -> Unit
) {
    val imageSize = 96.dp
    val cornerRadius = 12.dp
    val iconSize = 18.dp

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val titleFontSize = when {
        screenWidth < 320.dp -> 16.sp
        screenWidth < 360.dp -> 18.sp
        else -> 20.sp
    }

    Box(
        modifier = Modifier
            .padding(start = sixteenPad, end = sixteenPad, top = eightPad, bottom = eightPad)
            .clip(RoundedCornerShape(cornerRadius))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                itemFromDBViewModel.selectPlace(place)
                navController.navigate("placeItem")
            }
    ) {
        Row(
            modifier = Modifier.padding(twelvePad),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(8.dp))
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(place.imageURL)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Place image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(R.drawable.loading),
                    error = painterResource(R.drawable.placeholder_icon),
                    filterQuality = FilterQuality.High
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = twelvePad)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = place.name,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = titleFontSize,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = if (screenWidth < 360.dp) 24.dp else 32.dp) // Обеспечиваем место для иконки
                        )
                    }

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.favourite_icon),
                        contentDescription = "Удалить из избранного",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(2.dp)
                            .clickable {
                                userViewModel.removeFromFavourites(place.id.toString()) { success ->
                                    if (success) {
                                        onRemoveFromFavorites(place.id.toString())
                                    }
                                }
                            }
                    )
                }

                Spacer(modifier = Modifier.height(fourPad))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.map_pin_icon),
                        contentDescription = "Адрес",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(iconSize)
                    )
                    Text(
                        text = place.address,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = fourteenFontSize,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = fourPad)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                        contentDescription = "Рейтинг",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(iconSize)
                    )
                    Text(
                        text = place.rate.toString(),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = fourteenFontSize,
                        modifier = Modifier.padding(start = fourPad)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.clock_icon),
                        contentDescription = "Время работы",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(iconSize)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = isOpen(place.working_time),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = fourteenFontSize,
                            modifier = Modifier.padding(start = fourPad)
                        )
                    }
                }
            }
        }
    }
}