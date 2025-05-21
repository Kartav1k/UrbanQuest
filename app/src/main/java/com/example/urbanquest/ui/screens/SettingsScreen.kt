package com.example.urbanquest.ui.screens


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.domain.utils.constants.bigLabelSize
import com.example.urbanquest.domain.utils.constants.eightPad
import com.example.urbanquest.domain.utils.constants.eighteenFontSize
import com.example.urbanquest.domain.utils.constants.fourPad
import com.example.urbanquest.domain.utils.constants.labelSize
import com.example.urbanquest.domain.utils.constants.tenPad
import com.example.urbanquest.domain.utils.constants.thirtyTwoPad
import com.example.urbanquest.domain.utils.constants.twentyFourPad
import com.example.urbanquest.domain.utils.constants.twentyPad
import com.example.urbanquest.domain.utils.constants.twentyTwoFontSize
import com.example.urbanquest.ui.viewmodel.ThemeViewModel
import com.example.urbanquest.ui.viewmodel.UserViewModel

//Composable-функция настроек
@Composable
fun SettingsScreen(navController: NavHostController, userViewModel: UserViewModel, themeViewModel: ThemeViewModel){
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val context = LocalContext.current
        val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

        Row(
            modifier = Modifier.padding(bottom = eightPad, start = twentyPad)) {

            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(top = fourPad)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow_icon),
                    contentDescription = "Back button",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Text(
                text = stringResource(R.string.LABEL_settings),
                modifier = Modifier.padding(top = tenPad),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> labelSize
                    screenWidth > 360.dp -> bigLabelSize
                    else -> bigLabelSize
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(start = twentyFourPad, end = thirtyTwoPad)
                .fillMaxWidth(),
        ){
            Text(
                stringResource(R.string.change_theme),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> eighteenFontSize
                    screenWidth > 360.dp -> twentyTwoFontSize
                    else -> twentyTwoFontSize
                }
            )

            Spacer(Modifier.weight(1f, true))

            Switch(
                checked = isDarkTheme,
                onCheckedChange = {
                    themeViewModel.toggleTheme(it)
                    if (it) {
                        val achievementId = "dark_theme_switcher"
                        if (!userViewModel.hasAchievement(achievementId)) {
                            userViewModel.addAchievement(achievementId) { success ->
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        "Достижение разблокировано: Повелитель тьмы",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
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