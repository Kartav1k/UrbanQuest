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
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.ui.theme.Black
import com.example.urbanquest.ui.theme.White
import com.example.urbanquest.ui.theme.WhiteGrey
import com.example.urbanquest.ui.theme.linkColor

//Функция регистрации, пока без логики создания аккаунта с данными, пароль не скрывается и если перейти из меню назад, данные не сохраняются в строках
@Composable
fun Registration(navController: NavHostController, isAuthorization: Boolean){
    val password = remember { mutableStateOf("") }
    val confirmation_password = remember { mutableStateOf("") }
    val login = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
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

        TextField(login.value,
            placeholder = {
                Text("Логин", fontSize = 12.sp, color = WhiteGrey
                )},
            onValueChange = {
                login.value=it
            },
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
                .padding(start = 84.dp, end = 84.dp, bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Black,
                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                focusedTextColor = Black,
                unfocusedContainerColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )

        )
        TextField(phone.value,
            placeholder = {
                Text("Телефон", fontSize = 12.sp, color = WhiteGrey
                )},
            onValueChange = {
                phone.value=it
            },
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
                .padding(start = 84.dp, end = 84.dp, bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Black,
                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                focusedTextColor = Black,
                unfocusedContainerColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        TextField(password.value,
            placeholder = {
                Text("Пароль", fontSize = 12.sp, color = WhiteGrey
                )},
            onValueChange = {
                password.value=it
            },
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
                .padding(start = 84.dp, end = 84.dp, bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Black,
                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                focusedTextColor = Black,
                unfocusedContainerColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        TextField(confirmation_password.value,
            placeholder = {
                Text("Повторите пароль", fontSize = 12.sp, color = WhiteGrey
                )},
            onValueChange = {
                confirmation_password.value=it
            },
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
                .padding(start = 84.dp, end = 84.dp, bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Black,
                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                focusedTextColor = Black,
                unfocusedContainerColor = White,
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
            shape = RoundedCornerShape(15.dp)) {
            Text("Регистрация", color = MaterialTheme.colorScheme.tertiary, fontSize = 16.sp)

        }
    }
}