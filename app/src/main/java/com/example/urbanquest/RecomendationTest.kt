package com.example.urbanquest

import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.constants.LABEL_recomendationTest
import com.example.urbanquest.constants.create_recomendation_list
import com.example.urbanquest.constants.want_eat


//Добавить по кол-ву человек

@Composable
fun RecomendationTest(navController: NavHostController, viewModel: RecommendationViewModel) {

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
                text = LABEL_recomendationTest,
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
                text = want_eat,
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

        Button(
            onClick = {
                val selectedTags = checkBoxStates.mapIndexedNotNull { index, state ->
                    if (state.value) checkBoxLists.flatten()[index] else null
                } + foodCheckBoxStates.mapIndexedNotNull { index, state ->
                    if (state.value) foodCheckBoxLists.flatten()[index] else null
                }
                Log.d("SelectedTags", "Selected Tags: $selectedTags")

                viewModel.fetchRecommendations(selectedTags)
                navController.navigate("RecomendationList")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp, bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                create_recomendation_list,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}

@Composable
fun CheckBoxList(title: String, options: List<String>, checkBoxStates: List<MutableState<Boolean>>) {
    Column(modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 20.sp
        )

        options.forEachIndexed { index, option ->
            val isChecked = checkBoxStates[index]
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    modifier = Modifier
                        .padding()
                )
                Text(
                    text = option,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )
            }
        }
    }
}
