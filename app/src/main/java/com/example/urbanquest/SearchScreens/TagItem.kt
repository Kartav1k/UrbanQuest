package com.example.urbanquest.SearchScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TagItem(value: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = "$value",
            modifier = Modifier
                .align(Alignment.Center),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}