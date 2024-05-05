package com.example.urbanquest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.urbanquest.constants.LABEL_search
import com.example.urbanquest.constants.search_placeholder



@Composable
fun Search(navController: NavHostController, isAuthorization: Boolean){

    var searchRequest by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {

        Row(modifier = Modifier.padding(bottom = 8.dp, start = 20.dp)) {

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
                text = LABEL_search,
                fontSize = 32.sp,
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.tertiary
            )
        }


        TextField(
            value = searchRequest,
            onValueChange = {
                searchRequest=it
            },
            placeholder = {
                Text(text = search_placeholder, color = MaterialTheme.colorScheme.outlineVariant,
                    fontSize = 14.sp)
            },
            shape = RoundedCornerShape(90.dp),
            singleLine = true,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(Icons.Filled.Search,
                    contentDescription = "Search icon",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(end = 4.dp)
                )
            },
            trailingIcon = {
                if (searchRequest.isNotBlank()){
                    Icon(Icons.Filled.Clear,
                        contentDescription = "Clear icon",
                        tint=MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.clickable {
                            searchRequest = ""
                            keyboardController?.hide()
                        }
                    )
                }
            }
        )
    }
}