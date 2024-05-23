package com.example.urbanquest.ProfileScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.constants.LABEL_profile
import com.example.urbanquest.constants.achievements_text
import com.example.urbanquest.constants.exit_text
import com.example.urbanquest.constants.friends_text
import com.example.urbanquest.constants.info_text
import com.example.urbanquest.constants.settings_text


@Composable
fun ProfileScreen(navController: NavHostController, isAuthorization: Boolean){
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
                text = LABEL_profile,
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 32.sp
                    screenWidth > 360.dp -> 36.sp
                    else -> 36.sp
                }
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.no_avatar),
                contentDescription = "Avatar icon",
                modifier = Modifier.size(92.dp)
            )
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start) {

            Button(
                onClick = {

                },
                    modifier = Modifier
                        .padding(start = 12.dp, end = 32.dp)
                        .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)

            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()){
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.friends_icon),
                        contentDescription = "Friends",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(30.dp))
                    Text(
                        text = friends_text,
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
                        text = achievements_text,
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
                        text = settings_text,
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
                        text = info_text,
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
                        text = exit_text,
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