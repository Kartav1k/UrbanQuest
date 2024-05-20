package com.example.urbanquest.ProfileScreens


import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.constants.LABEL_settings
import com.example.urbanquest.constants.change_theme
import com.example.urbanquest.containers.getThemeState
import com.example.urbanquest.containers.setThemeState


@Composable
fun SettingsScreen(navController: NavHostController, isAuthorization: Boolean){
    Column(modifier = Modifier.verticalScroll(ScrollState(0))) {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val context = LocalContext.current
        var isDarkTheme = getThemeState(context)

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
                    imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow),
                    contentDescription = "Back button",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Text(
                text = LABEL_settings,
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 32.sp
                    screenWidth > 360.dp -> 36.sp
                    else -> 36.sp
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(start = 16.dp, end = 32.dp)
                .fillMaxWidth(),
        ){
            Text(change_theme,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 18.sp
                    screenWidth > 360.dp -> 22.sp
                    else -> 22.sp
                }
            )

            Spacer(Modifier.weight(1f, true))

            Switch(
                checked = isDarkTheme,
                onCheckedChange = {
                    isDarkTheme = it
                    setThemeState(context, isDarkTheme)
                },
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = MaterialTheme.colorScheme.secondaryContainer,
                    uncheckedBorderColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedBorderColor = MaterialTheme.colorScheme.primary,
                    checkedThumbColor = MaterialTheme.colorScheme.secondaryContainer,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}