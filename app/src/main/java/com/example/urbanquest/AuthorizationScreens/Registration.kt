package com.example.urbanquest.AuthorizationScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.constants.login_text
import com.example.urbanquest.constants.passwordRepeat_text
import com.example.urbanquest.constants.password_text
import com.example.urbanquest.constants.phone_text
import com.example.urbanquest.constants.registration_text

//Функция регистрации, пока без логики сохранения данных, пароль не скрывается и если перейти из меню назад, данные не сохраняются в строках
//Добавить ограничения на ввод, сокрытие паролей, сохранение аккаунта, и проверку на ошибки
@Composable
fun Registration(navController: NavHostController, isAuthorization: Boolean){
    val password = remember { mutableStateOf("") }
    val confirmationPassword = remember { mutableStateOf("") }
    val login = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(ScrollState(0)),
        horizontalAlignment = Alignment.CenterHorizontally) {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.iconforstartscreen),
            contentDescription = "Icon on start screen",
            alignment = Alignment.TopEnd,
            modifier = Modifier
                .padding(
                    start = 96.dp,
                    top = 8.dp,
                    end = 96.dp,
                    bottom = 16.dp
                )
        )
        TextField(
            login.value,
            placeholder = {
                Text(
                    login_text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )},
            onValueChange = {
                login.value=it
            },
            textStyle = TextStyle(fontSize = 14.sp),
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier

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
        TextField(
            phone.value,
            placeholder = {
                Text(
                    phone_text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )},
            onValueChange = {
                phone.value=it
            },
            textStyle = TextStyle(fontSize = 14.sp),
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
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

        TextField(
            password.value,
            placeholder = {
                Text(
                    password_text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )},
            onValueChange = {
                password.value=it
            },
            textStyle = TextStyle(fontSize = 14.sp),
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
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

        TextField(
            confirmationPassword.value,
            placeholder = {
                Text(
                    passwordRepeat_text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )},
            onValueChange = {
                confirmationPassword.value=it
            },
            textStyle = TextStyle(fontSize = 14.sp),
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
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
            shape = RoundedCornerShape(15.dp)) {
            Text(
                registration_text,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 14.sp
                    screenWidth > 360.dp -> 18.sp
                    else -> 18.sp
                }
            )
        }
    }
}