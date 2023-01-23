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
import com.github.pokatomnik.kriper.BuildConfig
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private fun Instant.formatCreationDate(format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format).withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

@Composable
fun BuildInfo() {
    val versionCode = BuildConfig.VERSION_CODE
    val versionName = BuildConfig.VERSION_NAME
    
    IndexServiceReadiness { indexService ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LARGE_PADDING.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "О приложении", fontWeight = FontWeight.Bold)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Версия: $versionCode ($versionName)")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                val dateStr = indexService.dateCreatedISO.formatCreationDate("dd.MM.yyyy")
                Text(text = "База историй за $dateStr")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Всего историй: ${indexService.content.allStoryTitles.size}")
            }
        }
    }
}