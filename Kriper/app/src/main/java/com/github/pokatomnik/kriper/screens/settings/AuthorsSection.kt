package com.github.pokatomnik.kriper.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING

@Composable
fun AuthorsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LARGE_PADDING.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Авторы", fontWeight = FontWeight.Bold)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("● Danilian Akhmedzianov (pokatomnik), pokatomnik@yandex.ru")
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("● rainbow666, rainbow666@kriper.net")
        }
    }
}