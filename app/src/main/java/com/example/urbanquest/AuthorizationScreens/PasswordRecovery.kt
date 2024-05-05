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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R

//Функция изменения пароля, пока без логики сохранения данных, пароль не скрывается, не сохраняется и изменяет в БД у пользователя, данные не сохраняются в строках
@Composable
fun PasswordRecovery(navController: NavHostController, isAuthorization: Boolean){
    val password = remember { mutableStateOf("") }
    val confirmationPassword = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.iconforstartscreen),
            contentDescription = "Иконка на начальном экране",
            alignment = Alignment.TopEnd,
            modifier = Modifier
                .padding(start = 96.dp, top = 24.dp, end = 96.dp, bottom = 24.dp)

        )

        TextField(phone.value,
            placeholder = {
                Text("Телефон", fontSize = 12.sp, color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            onValueChange = {
                phone.value=it
            },
            textStyle = TextStyle(fontSize = 14.sp),
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
                .padding(start = 84.dp, end = 84.dp, bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )

        )

        TextField(password.value,
            placeholder = {
                Text("Пароль", fontSize = 12.sp, color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            onValueChange = {
                password.value=it
            },
            textStyle = TextStyle(fontSize = 14.sp),
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
                .padding(start = 84.dp, end = 84.dp, bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        TextField(confirmationPassword.value,
            placeholder = {
                Text("Повторите пароль", fontSize = 12.sp, color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            onValueChange = {
                confirmationPassword.value=it
            },
            textStyle = TextStyle(fontSize = 14.sp),
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
                .padding(start = 84.dp, end = 84.dp, bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

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
            Text("Вход", color = MaterialTheme.colorScheme.tertiary, fontSize = 14.sp)

        }
    }
}