package com.example.urbanquest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(navController: NavHostController, isAuthorization: Boolean){
    Column {

        Row(modifier = Modifier.padding(bottom = 8.dp, start = 20.dp)) {

            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow),
                    contentDescription = "Back button",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Text(
                text = "Профиль",
                fontSize = 32.sp,
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.no_avatar),
            contentDescription = "Avatar icon",
            modifier = Modifier.padding(start = 148.dp, bottom = 24.dp))


        Column(verticalArrangement = Arrangement.Top) {


            Button(
                onClick = {

                },
                    modifier = Modifier.padding(start = 24.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.friends),
                    contentDescription = "Friends",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(30.dp))
                Text(
                    text = "Друзья",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )
            }


            Button(
                onClick = {

                },
                modifier = Modifier.padding(start = 24.dp)
            ) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Friends",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(end = 8.dp).size(30.dp))
                Text(
                    text = "Достижения",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )
            }


            Button(
                onClick = {

                },
                modifier = Modifier.padding(start = 24.dp)
            ) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Friends",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(end = 8.dp).size(30.dp))
                Text(
                    text = "Настройки",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )
            }


            Button(
                onClick = {

                },
                modifier = Modifier.padding(start = 24.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.info_icon),
                    contentDescription = "Friends",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(end = 8.dp).size(30.dp))
                Text(
                    text = "Информация о приложении",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )
            }


            Button(
                onClick = {

                },
                modifier = Modifier.padding(start = 24.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.exit_icon),
                    contentDescription = "Friends",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(end = 8.dp).size(30.dp))
                Text(
                    text = "Выход из профиля",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )
            }
        }
    }
}