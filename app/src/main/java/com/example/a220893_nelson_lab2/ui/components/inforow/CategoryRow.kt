package com.example.a220893_nelson_lab2.ui.components.inforow

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryRow(searchOn:(String)->Unit) {
    val categories = listOf("Electronics", "Sell","Donate","Fashion","Used")
    // only shows whats on viewport,lazyload
    LazyRow(
//        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            AssistChip(
                onClick = {
                    searchOn(category)
                },
                label = { Text(category) },
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
            )
        }
    }
}