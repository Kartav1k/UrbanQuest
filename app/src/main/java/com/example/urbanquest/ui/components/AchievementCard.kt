package com.example.urbanquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.urbanquest.R

@Composable
fun AchievementCard(achievementId: String, isUnlocked: Boolean) {
    // Данные о достижении на основе ID
    val (title, description, iconResId) = when (achievementId) {
        "dark_theme_switcher" -> Triple(
            "Повелитель тьмы",
            "Переключите тему на тёмную",
            R.drawable.eye_open // Замените на реальный ID
        )
        "first_recommendation_list" -> Triple(
            "Первооткрыватель",
            "Создайте свой первый список рекомендаций",
            R.drawable.map_icon// Замените на реальный ID
        )
        "app_info_visitor" -> Triple(
            "Любознательный исследователь",
            "Посетите раздел информации о приложении",
            R.drawable.info_icon // Замените на реальный ID
        )
        else -> Triple(
            "Неизвестное достижение",
            "Описание отсутствует",
            R.drawable.placeholder_icon // Замените на реальный ID
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = if (isUnlocked) 1f else 0.7f
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = if (isUnlocked) 0.2f else 0.1f
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = iconResId),
                    contentDescription = null,
                    tint = if (isUnlocked)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }

            // Индикатор состояния достижения
            if (isUnlocked) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Получено",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = "Не получено",
                    tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}