package com.example.urbanquest.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.ui.components.RecommendationItem
import com.example.urbanquest.ui.viewmodel.ItemFromDBViewModel
import com.example.urbanquest.ui.viewmodel.RecommendationViewModel

//Composable-функция списка рекомендаций
@Composable
fun Recommendations(
    navController: NavHostController,
    viewModel: RecommendationViewModel,
    itemFromDBViewModel: ItemFromDBViewModel
) {
    val recommendations by viewModel.recommendations.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isError by viewModel.isError.observeAsState(false)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Log.d("Recommendations", "Number of recommendations: ${recommendations.size}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Row(modifier = Modifier.padding(bottom = 8.dp, start = 18.dp)) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow_icon),
                    contentDescription = "Back button",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Text(
                text = stringResource(R.string.LABEL_recomendationList),
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 28.sp
                    screenWidth > 360.dp -> 30.sp
                    else -> 30.sp
                }
            )
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (isError) {
            Text(
                stringResource(R.string.error_warning),
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally))
        } else {
            if (recommendations.isEmpty()) {
                Text(
                    stringResource(R.string.no_result),
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 32.dp),
                    maxLines = 2
                )
            } else {
                recommendations.forEach { item ->
                    RecommendationItem(item, navController, itemFromDBViewModel)
                }
            }
        }
    }
}
