package com.github.pokatomnik.kriper.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.components.StatisticsProgressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

@Composable
fun rememberStatisticsData(): Pair<Int, Int>? {
    val database = rememberKriperDatabase()
    val (statistics, setStatistics) = remember { mutableStateOf<Pair<Int, Int>?>(null) }
    LaunchedEffect(database) {
        withContext(Dispatchers.IO + SupervisorJob()) {
            val readStories = database.historyDAO().getAllReadStoriesIdSet().size
            val historySize = database.historyDAO().getAll().size
            setStatistics(Pair(readStories, historySize))
        }
    }
    return statistics
}

@Composable
fun StatisticsBlock() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SMALL_PADDING.dp)
        ) {
            Spacer(modifier = Modifier.width(LARGE_PADDING.dp))
            Text(
                text = "СТАТИСТИКА",
                fontWeight = FontWeight.Bold
            )
        }
    }
    Spacer(modifier = Modifier.height(SMALL_PADDING.dp))
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IndexServiceReadiness { indexService ->
            val totalStories = indexService.content.selections.allStoryTitles.size
            val statisticsData = rememberStatisticsData()
            val readStories = statisticsData?.first ?: 0
            val historySize = statisticsData?.second ?: 0
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                StatisticsProgressBar(
                    title = "Прочитано",
                    current = readStories,
                    max = totalStories,
                    animationMillis = 1000
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                StatisticsProgressBar(
                    title = "Просмотрено",
                    current = historySize,
                    max = totalStories,
                    animationMillis = 1000
                )
            }
        }
    }
}