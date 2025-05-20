package com.example.urbanquest.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.ui.viewmodel.UserViewModel
import com.example.urbanquest.R


@Composable
fun ProfileScreen(navController: NavHostController, userViewModel: UserViewModel){

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (userViewModel.isAuthorized.value) {
            isLoading = true
        } else {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        Row(
            modifier = Modifier.padding(bottom = 8.dp, start = 20.dp)) {

            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow_icon),
                    contentDescription = "Back button",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Text(
                text = stringResource(R.string.LABEL_profile),
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 32.sp
                    screenWidth > 360.dp -> 36.sp
                    else -> 36.sp
                }
            )
        }

        if (!userViewModel.isAuthorized.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Войдите, чтобы получить доступ к избранному",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate("Choice_authorization")
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(45.dp)
                    ) {
                        Text(
                            "Войти",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        } else if (isLoading) {
            Column(

                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start) {

                Button(
                    onClick = {
                        navController.navigate("Achievements")
                    },
                    modifier = Modifier
                        .padding(start = 12.dp, end = 32.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                            contentDescription = "Achievements",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(30.dp)
                        )
                        Text(
                            text = stringResource(R.string.achievements_text),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = when {
                                screenWidth <= 360.dp -> 18.sp
                                screenWidth > 360.dp -> 22.sp
                                else -> 22.sp
                            }
                        )
                    }
                }


                Button(
                    onClick = {
                        navController.navigate("Settings")
                    },
                    modifier = Modifier
                        .padding(start = 12.dp, end = 32.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.settings_icon),
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(30.dp)
                        )
                        Text(
                            text = stringResource(R.string.settings_text),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = when {
                                screenWidth <= 360.dp -> 18.sp
                                screenWidth > 360.dp -> 22.sp
                                else -> 22.sp
                            }
                        )
                    }
                }


                Button(
                    onClick = {
                        navController.navigate("InfoAboutApp")
                    },
                    modifier = Modifier
                        .padding(start = 12.dp, end = 32.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.info_icon),
                            contentDescription = "Info about App",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(30.dp)
                        )
                        Text(
                            text = stringResource(R.string.info_text),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = when {
                                screenWidth <= 360.dp -> 18.sp
                                screenWidth > 360.dp -> 20.sp
                                else -> 22.sp
                            }
                        )
                    }
                }


                Button(
                    onClick = {
                        userViewModel.logout()
                        navController.navigate("Choice_authorization") {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier
                        .padding(start = 12.dp, end = 32.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.exit_icon),
                            contentDescription = "Exit from App",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(35.dp)
                        )
                        Text(
                            text = stringResource(R.string.exit_text),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = when {
                                screenWidth <= 360.dp -> 18.sp
                                screenWidth > 360.dp -> 22.sp
                                else -> 22.sp
                            }
                        )
                    }
                }
            }
        }
    }
}