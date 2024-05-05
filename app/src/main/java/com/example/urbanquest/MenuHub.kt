package com.example.urbanquest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun MenuHub(navController: NavHostController, isAuthorization: Boolean){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        ){

        Text(
            text = "Меню",
            fontSize = 32.sp,
            modifier = Modifier.padding(start = 32.dp, top = 10.dp, bottom = 24.dp),
            color = MaterialTheme.colorScheme.tertiary
        )

        Button(
            onClick = {

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
            shape = RoundedCornerShape(90.dp)
        ){
            Column(horizontalAlignment = Alignment.Start) {
                Text("Куда вы хотите сходить?",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight= FontWeight(495),
                    modifier = Modifier
                        .padding(top=8.dp,end=16.dp)
                )
                Text("Составьте свой список рекомендаций",
                    fontSize = 10.sp,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.tertiary)
            }
        }

        Text("Ваш список рекомендаций",
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(start = 32.dp, bottom = 8.dp),
            fontWeight= FontWeight(495)
        )

        Button(
            onClick = {

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
            Text("Посмотрите ваш список рекомендаций",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary)
        }


        Row(horizontalArrangement = Arrangement.Center) {

            Button(
                onClick = {

                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .height(128.dp)
                    .padding(start = 44.dp, end = 16.dp)
                    .shadow(
                        elevation = 15.dp,
                        shape = RoundedCornerShape(4.dp),
                        clip = false,
                        ambientColor = Color.Black.copy(alpha = 0.2f)
                    ),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Text("Места для прогулок",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.tertiary)
            }

            Button(
                onClick = {

                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .height(128.dp)
                    .padding(start = 16.dp, end = 44.dp)
                    .shadow(
                        elevation = 15.dp,
                        shape = RoundedCornerShape(4.dp),
                        clip = false,
                        ambientColor = Color.Black.copy(alpha = 0.2f)
                    ),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Text("Рестораны и кафе",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.tertiary)
            }
        }
    }
}

