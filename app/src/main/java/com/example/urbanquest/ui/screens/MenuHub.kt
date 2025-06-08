package com.example.urbanquest.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//Composable-функция главного экрана с навигацией
@Composable
fun MenuHub(navController: NavHostController){
    var isNavigatingToWalking by remember { mutableStateOf(false) }
    var isNavigatingToFood by remember { mutableStateOf(false) }
    var isNavigatingToRecommendations by remember { mutableStateOf(false) }
    var isNavigatingToRecommendationTest by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ){

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        Text(
            text = stringResource(R.string.LABEL_menu),
            modifier = Modifier.padding(start = 32.dp, top = 12.dp, bottom = 16.dp),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = when {
                screenWidth <= 360.dp -> 32.sp
                screenWidth > 360.dp -> 36.sp
                else -> 36.sp
            }
        )

        Button(
            onClick = {
                if (!isNavigatingToRecommendationTest) {
                    isNavigatingToRecommendationTest = true
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(300)
                        navController.navigate("RecomendationTest")
                        isNavigatingToRecommendationTest = false
                    }
                }
            },
            modifier = Modifier
                .height(80.dp)
                .padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 15.dp,
                    shape = RoundedCornerShape(4.dp),
                    clip = false,
                    ambientColor = Color.Black.copy(alpha = 0.2f)
                ),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            shape = RoundedCornerShape(45.dp)
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = if (isNavigatingToRecommendationTest) Alignment.CenterHorizontally else Alignment.Start
            ) {
                if (isNavigatingToRecommendationTest) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else {
                    Text(
                        stringResource(R.string.question_text),
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight= FontWeight(495),
                        modifier = Modifier
                            .padding(top=8.dp,end=16.dp),
                        fontSize = when {
                            screenWidth <= 360.dp -> 18.sp
                            screenWidth > 360.dp -> 22.sp
                            else -> 22.sp
                        }
                    )
                    Text(
                        stringResource(R.string.doRecommendationList_text),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        Text(
            stringResource(R.string.LABEL_recomendation),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(start = 32.dp, bottom = 8.dp),
            fontWeight= FontWeight(495) ,
            fontSize = when {
                screenWidth <= 360.dp -> 24.sp
                screenWidth > 360.dp -> 26.sp
                else -> 26.sp
            }
        )
        Button(
            onClick = {
                if (!isNavigatingToRecommendations) {
                    isNavigatingToRecommendations = true
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(300)
                        navController.navigate("RecomendationList")
                        isNavigatingToRecommendations = false
                    }
                }
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .height(110.dp)
                .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 15.dp,
                    shape = RoundedCornerShape(4.dp),
                    clip = false,
                    ambientColor = Color.Black.copy(alpha = 0.2f)
                ),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ){
                if (isNavigatingToRecommendations) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else {
                    Text(
                        stringResource(R.string.recommendationList_text),
                        color = MaterialTheme.colorScheme.tertiary,
                        textAlign = TextAlign.Center,
                        softWrap = true,
                        fontSize = when {
                            screenWidth <= 360.dp -> 15.sp
                            screenWidth > 360.dp -> 18.sp
                            else -> 18.sp
                        }
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (!isNavigatingToWalking) {
                        isNavigatingToWalking = true
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(300)
                            navController.navigate("WalkingPlaces")
                            isNavigatingToWalking = false
                        }
                    }
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(128.dp)
                    .padding(start = 32.dp)
                    .shadow(
                        elevation = 15.dp,
                        shape = RoundedCornerShape(4.dp),
                        clip = false,
                        ambientColor = Color.Black.copy(alpha = 0.2f)
                    ),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isNavigatingToWalking) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    } else {
                        Text(
                            stringResource(R.string.walkingPlaces_text),
                            color = MaterialTheme.colorScheme.tertiary,
                            textAlign = TextAlign.Center,
                            softWrap = true,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (!isNavigatingToFood) {
                        isNavigatingToFood = true
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(300)
                            navController.navigate("FoodPlaces")
                            isNavigatingToFood = false
                        }
                    }
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(128.dp)
                    .padding(start = 8.dp, end = 32.dp)
                    .shadow(
                        elevation = 15.dp,
                        shape = RoundedCornerShape(4.dp),
                        clip = false,
                        ambientColor = Color.Black.copy(alpha = 0.2f)
                    ),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isNavigatingToFood) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    } else {
                        Text(
                            stringResource(R.string.foodPlaces_text),
                            color = MaterialTheme.colorScheme.tertiary,
                            textAlign = TextAlign.Center,
                            softWrap = true,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}