package com.example.urbanquest.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.R
import com.example.urbanquest.ui.components.CheckBoxList
import com.example.urbanquest.ui.viewmodel.RecommendationViewModel
import com.example.urbanquest.ui.viewmodel.UserViewModel


//Добавить по кол-ву человек
//Composable-функция теста рекомендаций
@Composable
fun RecomendationTest(navController: NavHostController, viewModel: RecommendationViewModel, userViewModel: UserViewModel) {

    val checkBoxLists = listOf(
        listOf("Один", "С парой", "С друзьями", "С коллегами", "С семьей"),
        listOf("Парк", "Природа", "Вода", "Пляж", "Экотропа", "Пикник", "Ботанический сад", "Животные", "Бассейн"),
        listOf("Активный отдых", "Спорт", "Развлечение", "Велопрогулка", "Пешая прогулка", "Водный отдых", "Коворкинг"),
        listOf("Памятник", "История", "Музей", "Архитектура", "Театр", "Экскурсия", "Религия", "Смотровая площадка", "Фотозона", "Зарубежное", "Красивый вид", "Достопримечательность"),
        listOf("Кинотеатр", "Аттракционы", "Торговый центр", "Шоппинг", "Современное пространство", "Ночная жизнь")
    )

    val foodCheckBoxLists = listOf(
        listOf("Японская кухня", "Грузинская кухня", "Восточная кухня", "Американская кухня", "Русская кухня", "Корейская кухня", "Испанская кухня", "Итальянская кухня"),
        listOf("Фаст-фуд", "Пицца", "Суши", "Стейкхаус", "Завтраки", "Здоровое питание", "Шаурма", "Мясное", "Выпечка", "Супы", "Морепродукты", "Блюда национальной кухни"),
        listOf("Бургерная", "Кафе", "Кофейня", "Столовая", "Ресторан", "Бар", "Кондитерская", "Пекарня", "Антикафе", "Коворкинг", "Уникальный интерьер")
    )

    val titles = listOf("Количество отдыхающих", "Места для прогулки", "Активности", "Достопримечательности", "Развлечения")
    val foodTitles = listOf("Вид кухни", "Варианты еды", "Устройство заведения")

    val checkBoxStates = remember { checkBoxLists.flatten().map { mutableStateOf(false) } }
    val foodCheckBoxStates = remember { foodCheckBoxLists.flatten().map { mutableStateOf(false) } }
    var showFoodOptions by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var recommendationCount by remember { mutableStateOf(5) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        Row(modifier = Modifier.padding(bottom = 8.dp, start = 20.dp)) {
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
                text = stringResource(R.string.LABEL_recomendationTest),
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = when {
                    screenWidth <= 360.dp -> 28.sp
                    screenWidth > 360.dp -> 30.sp
                    else -> 30.sp
                }
            )
        }

        var currentIndex = 0
        checkBoxLists.forEachIndexed { index, list ->
            val sublistSize = list.size
            CheckBoxList(
                title = titles[index],
                options = list,
                checkBoxStates = checkBoxStates.subList(currentIndex, currentIndex + sublistSize)
            )
            currentIndex += sublistSize
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Checkbox(
                checked = showFoodOptions,
                onCheckedChange = { showFoodOptions = it }
            )
            Text(
                text = stringResource(R.string.want_eat),
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        if (showFoodOptions) {
            var foodIndex = 0
            foodCheckBoxLists.forEachIndexed { index, list ->
                val sublistSize = list.size
                CheckBoxList(
                    title = foodTitles[index],
                    options = list,
                    checkBoxStates = foodCheckBoxStates.subList(foodIndex, foodIndex + sublistSize)
                )
                foodIndex += sublistSize
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Количество рекомендаций: $recommendationCount",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Slider(
                value = recommendationCount.toFloat(),
                onValueChange = { recommendationCount = it.toInt() },
                valueRange = 1f..10f,
                steps = 9,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "Будет показано до $recommendationCount мест для прогулок и до $recommendationCount заведений",
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
        }
        Button(
            onClick = {
                viewModel.setRecommendationLimit(recommendationCount)
                val selectedTags = checkBoxStates.mapIndexedNotNull { index, state ->
                    if (state.value) checkBoxLists.flatten()[index] else null
                } + foodCheckBoxStates.mapIndexedNotNull { index, state ->
                    if (state.value) foodCheckBoxLists.flatten()[index] else null
                }
                Log.d("SelectedTags", "Selected Tags: $selectedTags")
                val achievementId = "first_recommendation_list"
                if (!userViewModel.hasAchievement(achievementId)) {
                    userViewModel.addAchievement(achievementId) { success ->
                        if (success) {
                            Toast.makeText(
                                context,
                                "Достижение разблокировано: Первооткрыватель",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                viewModel.fetchRecommendations(selectedTags)
                navController.navigate("RecomendationList")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp, bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                stringResource(R.string.create_recomendation_list),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}
