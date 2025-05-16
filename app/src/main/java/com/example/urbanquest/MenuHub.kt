package com.example.urbanquest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.urbanquest.AuthorizationScreens.UserViewModel

//Composable-функция главного экрана с навигацией

@Composable
fun MenuHub(navController: NavHostController, userViewModel: UserViewModel){
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
                navController.navigate("RecomendationTest")
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
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
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
                    color = MaterialTheme.colorScheme.tertiary)
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
                navController.navigate("RecomendationList")
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .height(110.dp)
                .padding(start = 44.dp, end = 44.dp, bottom = 16.dp)
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


        Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {

            Button(
                onClick = {
                    navController.navigate("WalkingPlaces")
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(128.dp)
                    .padding(start = 44.dp)
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
                    Text(
                        stringResource(R.string.walkingPlaces_text),
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

            Button(
                onClick = {
                    navController.navigate("FoodPlaces")
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(128.dp)
                    .padding(start = 8.dp, end = 44.dp)
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
                    Text(
                        stringResource(R.string.foodPlaces_text),
                        color = MaterialTheme.colorScheme.tertiary,
                        textAlign = TextAlign.Center,  // Центрирование текста
                        softWrap = true,   // Разрешает перенос текста
                        fontSize = when {
                            screenWidth <= 360.dp -> 15.sp
                            screenWidth > 360.dp -> 18.sp
                            else -> 18.sp
                        }
                    )
                }
            }
        }
    }
}

