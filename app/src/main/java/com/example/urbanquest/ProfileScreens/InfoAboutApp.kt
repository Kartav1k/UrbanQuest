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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.constants.LABEL_info
import com.example.urbanquest.constants.name_app
import com.example.urbanquest.constants.name_app_text
import com.example.urbanquest.constants.version_app
import com.example.urbanquest.constants.yandex_terms_of_use_of_the_service
import com.example.urbanquest.ui.theme.linkColor

@Composable
fun InfoAboutApp(navController: NavHostController, isAuthorization: Boolean){

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val screenWidth = configuration.screenWidthDp.dp

    LazyColumn {

        item {
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
                    text = LABEL_info,
                    modifier = Modifier.padding(top = 10.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = when {
                        screenWidth <= 360.dp -> 32.sp
                        screenWidth > 360.dp -> 36.sp
                        else -> 36.sp
                    }
                )
            }
        }
        item {
            Text(
                name_app_text + name_app,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 18.sp
                    screenWidth > 360.dp -> 22.sp
                    else -> 22.sp
                }
            )
        }

        item {
            Text(yandex_terms_of_use_of_the_service,
                color = linkColor,
                fontSize = when {
                    screenWidth <= 360.dp -> 18.sp
                    screenWidth > 360.dp -> 22.sp
                    else -> 22.sp
                },
                modifier = Modifier
                    .padding(start = 16.dp)
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
            Text(version_app+"close beta",
                modifier = Modifier.padding(16.dp),
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