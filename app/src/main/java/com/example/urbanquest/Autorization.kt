package com.example.urbanquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.ui.theme.linkColor


//Доделать ввод текста
@Composable
fun Authorization(navController: NavHostController){
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.iconforstartscreen),
            contentDescription = "Иконка на начальном экране",
            alignment = Alignment.TopEnd,
            modifier = Modifier
                .padding(start = 68.dp, top = 48.dp, end = 68.dp, bottom = 24.dp)
                .size(224.dp)
                .fillMaxSize()
        )

        TextField(value = "",
            placeholder = {
                Text("Логин", fontSize = 12.sp
                )
            },
            onValueChange = {

            },
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
                .padding(start = 84.dp, end = 84.dp, bottom = 16.dp)
        )
        TextField(value = "",
            placeholder = {
                Text("Пароль", fontSize = 12.sp
                )
            },
            onValueChange = {

            },
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
                .padding(start = 84.dp, end = 84.dp, bottom = 3.dp)
        )
        Row(){
            Text("Забыли пароль? ", color = MaterialTheme.colorScheme.tertiary)
            Text("Восстановите", color = linkColor, modifier = Modifier
                .padding(bottom = 16.dp))
        }
        Button(
            onClick = {
                navController.navigate("MenuHub")
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(start = 84.dp, end = 84.dp)
                .height(height = 52.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("Вход", color = MaterialTheme.colorScheme.tertiary, fontSize = 16.sp)

        }
    }

}