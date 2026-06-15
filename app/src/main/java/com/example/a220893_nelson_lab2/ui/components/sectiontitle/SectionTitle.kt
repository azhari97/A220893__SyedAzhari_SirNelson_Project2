package com.example.a220893_nelson_lab2.ui.components.sectiontitle

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(text:String){
    Text(text=text, modifier = Modifier.padding(12.dp))
}