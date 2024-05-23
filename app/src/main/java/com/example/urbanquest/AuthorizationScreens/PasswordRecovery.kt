package com.example.urbanquest.AuthorizationScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.constants.email_text
import com.example.urbanquest.constants.entrance_text
import com.example.urbanquest.constants.passwordRepeat_text
import com.example.urbanquest.constants.password_text

//Функция изменения пароля, пока без логики сохранения данных, не сохраняется и изменяет в БД у пользователя
@Composable
fun PasswordRecovery(navController: NavHostController, isAuthorization: Boolean){
    var password by rememberSaveable { mutableStateOf("") }
    var confirmationPassword by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var isVisibleForPassword by rememberSaveable { mutableStateOf(false) }
    var isVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.iconforstartscreen),
            contentDescription = "Icon on start screen",
            alignment = Alignment.TopEnd,
            modifier = Modifier
                .padding(start = 96.dp, top = 24.dp, end = 96.dp, bottom = 24.dp)

        )

        TextField(
            email,
            placeholder = {
                Text(
                    email_text,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            onValueChange = {
                email = it
            },
            textStyle = TextStyle(fontSize = 12.sp),
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
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
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            onValueChange = {
                password = it
            },
            visualTransformation = if (isVisibleForPassword) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = TextStyle(fontSize = 12.sp),
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 84.dp, end = 84.dp, bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                val passwordIcon = if (isVisibleForPassword){
                    ImageVector.vectorResource(id = R.drawable.eye_open)
                }
                else{
                    ImageVector.vectorResource(id = R.drawable.eye_close)
                }
                if (password.isNotBlank()) {
                    Icon(
                        passwordIcon,
                        contentDescription = "Opeb password icon",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                isVisibleForPassword = !isVisibleForPassword
                            }
                    )
                }
            }
        )

        TextField(
            confirmationPassword,
            placeholder = {
                Text(
                    passwordRepeat_text,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            onValueChange = {
                confirmationPassword = it
            },
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = TextStyle(fontSize = 12.sp),
            shape = RoundedCornerShape(45.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 84.dp, end = 84.dp, bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                val passwordIcon = if (isVisible){
                    ImageVector.vectorResource(id = R.drawable.eye_open)
                }
                else{
                    ImageVector.vectorResource(id = R.drawable.eye_close)
                }
                if (confirmationPassword.isNotBlank()) {
                    Icon(
                        passwordIcon,
                        contentDescription = "Opeb password icon",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                isVisible = !isVisible
                            }
                    )
                }
            }
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
            Text(
                entrance_text,
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