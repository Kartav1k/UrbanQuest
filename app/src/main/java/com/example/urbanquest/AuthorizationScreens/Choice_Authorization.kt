package com.example.urbanquest.AuthorizationScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.constants.authorization_text
import com.example.urbanquest.constants.guest_text
import com.example.urbanquest.constants.registration_text


//Начальный экран, где выбор куда перейти: на регистрацию, на авторизацию или войти без входа с урезанным функционалом(не реализовано)
@Composable
fun ChoiceAuthorization(navController: NavHostController){
    
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.iconforstartscreen),
            contentDescription = "Icon on start screen",
            alignment = Alignment.TopEnd,
            modifier = Modifier
                .padding(start = 96.dp, top = 24.dp, end = 96.dp, bottom = 24.dp)

        )

        Button(
            onClick = {
                navController.navigate("Registration")
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(start = 84.dp, bottom = 16.dp, end = 84.dp)
                .height(height = 52.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
            ) {
            Text(
                registration_text,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 14.sp)
            
        }

        Button(
            onClick = {
                navController.navigate("Authorization")
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(start = 84.dp, bottom = 16.dp, end = 84.dp)
                .height(height = 52.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)) {
            Text(
                authorization_text,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 14.sp)
        }

        Button(
            onClick = {
                navController.navigate("MenuHub")
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(start = 84.dp, bottom = 16.dp, end = 84.dp)
                .height(height = 52.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)) {
            Text(
                guest_text,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 14.sp)

        }

    }
}


