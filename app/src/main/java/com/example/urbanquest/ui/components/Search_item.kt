package com.example.urbanquest.ui.components

import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//Composable-функция для отображения элемента списка в поиске
@Composable
fun SearchItem(
    context: Context,
    place: ItemFromDB,
    navController: NavHostController,
    itemFromDBViewModel: ItemFromDBViewModel,
    userViewModel: UserViewModel
) {
    val isFavorite = remember { mutableStateOf(userViewModel.isFavourite(place.id.toString())) }
    var isNavigating by remember { mutableStateOf(false) }

    LaunchedEffect(userViewModel.userData.value) {
        isFavorite.value = userViewModel.isFavourite(place.id.toString())
    }

    // Адаптивные размеры на основе констант
    val imageSize = 96.dp
    val cornerRadius = 12.dp
    val iconSize = 18.dp

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Адаптивный размер шрифта на основе ширины экрана
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
            .clickable(enabled = !isNavigating) {
                isNavigating = true
                itemFromDBViewModel.selectPlace(place)
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100)
                    navController.navigate("placeItem")
                    isNavigating = false
                }
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

            // Информация о месте
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
                    // Название места с горизонтальным скроллом
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
                            modifier = Modifier.padding(end = if (screenWidth < 360.dp) 24.dp else 32.dp)
                        )
                    }

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.favourite_icon),
                        contentDescription = "Избранное",
                        tint = if (isFavorite.value) Color.Red else MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(26.dp)
                            .padding(2.dp)
                            .clickable {
                                if (userViewModel.isAuthorized.value) {
                                    if (isFavorite.value) {
                                        userViewModel.removeFromFavourites(place.id.toString()) { success ->
                                            if (success) {
                                                isFavorite.value = false
                                            }
                                        }
                                    } else {
                                        userViewModel.addToFavourites(place.id.toString()) { success ->
                                            if (success) {
                                                isFavorite.value = true
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Войдите, чтобы добавить в избранное",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    )
                }

                Spacer(modifier = Modifier.height(fourPad))

                // Адрес
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

                // Рейтинг
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

                // Время работы с горизонтальным скроллом
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

                    // Используем Box с горизонтальным скроллом для длинных строк времени работы
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