package com.example.urbanquest.AuthorizationScreens

import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.constants.email_text
import com.example.urbanquest.constants.login_text
import com.example.urbanquest.constants.passwordRepeat_text
import com.example.urbanquest.constants.password_text
import com.example.urbanquest.constants.registration_text

//Функция регистрации, пока данные не сохраняются в строках при переходе назад


@Composable
fun Registration(navController: NavHostController, isAuthorization: Boolean){

    var password by remember { mutableStateOf("") }
    var confirmationPassword by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
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
            login,
            placeholder = {
                Text(
                    login_text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )},
            onValueChange = {
                login = it
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
            email,
            placeholder = {
                Text(
                    email_text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )},
            onValueChange = {
                email = it
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
            password,
            placeholder = {
                Text(
                    password_text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )},
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                password = it
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
            confirmationPassword,
            placeholder = {
                Text(
                    passwordRepeat_text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )},
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                confirmationPassword = it
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
                if (password == confirmationPassword
                    && isEmailEmpty(email)
                    && isLoginEmpty(login)
                    && isEmailCorrect(email)
                    && isPasswordCorrect(password)
                    && isLoginCorrect(login))
                {
                    registrationUser(email, login, password)
                    navController.navigate("MenuHub")
                } else {
                    //Добавить вывод Тостов с определенной проблемой, а не в общем случае
                    Toast.makeText(context, "Недопустимое значение в одном из полей, исправьте его" , Toast.LENGTH_SHORT).show()
                }
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