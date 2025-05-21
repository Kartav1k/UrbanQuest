package com.example.urbanquest.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.ui.components.AchievementCard
import com.example.urbanquest.ui.viewmodel.UserViewModel


//Composable-функция экрана с достижениями
@Composable
fun AchievementsScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Получаем список разблокированных достижений
    val unlockedAchievements = userViewModel.getAchievements()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Заголовок
        item {
            Row(
                modifier = Modifier.padding(bottom = 8.dp, start = 20.dp)
            ) {
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
                    text = stringResource(R.string.LABEL_achievements),
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

        // Секция разблокированных достижений
        if (unlockedAchievements.isNotEmpty()) {
            item {
                Text(
                    text = "Полученные достижения",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(unlockedAchievements.size) { index ->
                val achievementId = unlockedAchievements[index]
                AchievementCard(
                    achievementId = achievementId,
                    isUnlocked = true
                )
            }
        }

        // Секция заблокированных достижений
        item {
            Text(
                text = if (unlockedAchievements.isEmpty()) "Доступные достижения" else "Еще не получены",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        // Достижение: Переключить тему на тёмную
        item {
            val achievementId = "dark_theme_switcher"
            if (!unlockedAchievements.contains(achievementId)) {
                AchievementCard(
                    achievementId = achievementId,
                    isUnlocked = false
                )
            }
        }

        // Достижение: Создать первый список рекомендаций
        item {
            val achievementId = "first_recommendation_list"
            if (!unlockedAchievements.contains(achievementId)) {
                AchievementCard(
                    achievementId = achievementId,
                    isUnlocked = false
                )
            }
        }

        // Достижение: Зайти в раздел информации о приложении
        item {
            val achievementId = "app_info_visitor"
            if (!unlockedAchievements.contains(achievementId)) {
                AchievementCard(
                    achievementId = achievementId,
                    isUnlocked = false
                )
            }
        }
    }
}