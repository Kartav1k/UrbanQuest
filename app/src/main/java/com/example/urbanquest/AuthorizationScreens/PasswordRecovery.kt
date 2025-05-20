package com.example.urbanquest.AuthorizationScreens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R

//Composable-функция изменения пароля, пока без логики сохранения данных, не сохраняется и изменяет в БД у пользователя

@Composable
fun PasswordRecovery(navController: NavHostController, userViewModel: UserViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    val context = LocalContext.current


    LaunchedEffect(showToast) {
        if (showToast) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            showToast = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.iconforstartscreen),
            contentDescription = "Icon on start screen",
            alignment = Alignment.TopEnd,
            modifier = Modifier
                .padding(start = 96.dp, top = 24.dp, end = 96.dp, bottom = 24.dp)
        )

        Text(
            text = "Восстановление пароля",
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Введите email для получения ссылки на сброс пароля",
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp)
        )

        // Поле email
        TextField(
            value = email,
            placeholder = {
                Text(
                    stringResource(R.string.email_text),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            onValueChange = {
                email = it
                errorMessage = null
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
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )

        // Отображение ошибки
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 84.dp, vertical = 8.dp)
            )
        }

        // Кнопка отправки
        Button(
            onClick = {
                if (email.isEmpty()) {
                    errorMessage = "Введите email"
                    return@Button
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    errorMessage = "Некорректный email"
                    return@Button
                }

                isLoading = true
                userViewModel.resetPassword(email) { success, message ->
                    isLoading = false
                    if (success) {
                        toastMessage = "Письмо для восстановления пароля отправлено на почту"
                        showToast = true
                        navController.popBackStack()
                    } else {
                        toastMessage = message ?: "Ошибка при отправке письма"
                        showToast = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(start = 84.dp, end = 84.dp)
                .height(height = 52.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    "Отправить",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = when {
                        screenWidth <= 360.dp -> 14.sp
                        screenWidth > 360.dp -> 18.sp
                        else -> 18.sp
                    }
                )
            }
        }

        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                "Вернуться назад",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 14.sp
            )
        }
    }
}