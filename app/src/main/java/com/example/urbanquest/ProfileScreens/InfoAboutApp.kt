package com.example.urbanquest.ProfileScreens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.constants.bigLabelSize
import com.example.urbanquest.constants.eightPad
import com.example.urbanquest.constants.fourPad
import com.example.urbanquest.constants.labelSize
import com.example.urbanquest.constants.sixteenFontSize
import com.example.urbanquest.constants.sixteenPad
import com.example.urbanquest.constants.tenPad
import com.example.urbanquest.constants.twelvePad
import com.example.urbanquest.constants.twentyPad
import com.example.urbanquest.constants.twentyTwoFontSize
import com.example.urbanquest.ui.theme.linkColor

@Composable
fun InfoAboutApp(navController: NavHostController, isAuthorization: Boolean){

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val screenWidth = configuration.screenWidthDp.dp

    LazyColumn {

        item {
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
                    text = stringResource(R.string.LABEL_info),
                    modifier = Modifier.padding(top = tenPad),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = when {
                        screenWidth <= 360.dp -> labelSize
                        screenWidth > 360.dp -> bigLabelSize
                        else -> bigLabelSize
                    }
                )
            }
        }
        item {
            Text(
                stringResource(R.string.name_app_text) + stringResource(R.string.name_app),
                modifier = Modifier.padding(start = sixteenPad, bottom = twelvePad),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> sixteenFontSize
                    screenWidth > 360.dp -> twentyTwoFontSize
                    else -> twentyTwoFontSize
                }
            )
        }

        item {
            Text(stringResource(R.string.yandex_terms_of_use_of_the_service),
                color = linkColor,
                fontSize = when {
                    screenWidth <= 360.dp -> 16.sp
                    screenWidth > 360.dp -> 22.sp
                    else -> 22.sp
                },
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 12.dp)
                    .clickable {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://yandex.ru/legal/maps_termsofuse/")
                        )
                        context.startActivity(intent)
                    }
            )
        }

        item {
            Text(
                stringResource(R.string.version_app)+"close beta",
                modifier = Modifier.padding(start = 16.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 16.sp
                    screenWidth > 360.dp -> 22.sp
                    else -> 22.sp
                }
            )
        }

    }
}