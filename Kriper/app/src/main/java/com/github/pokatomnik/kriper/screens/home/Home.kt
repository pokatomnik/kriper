package com.github.pokatomnik.kriper.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Tag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING

@Composable
private fun HomeHorizontalSpacer() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(SMALL_PADDING.dp)
    )
}

@Composable
fun Home(
    onNavigateToTagGroups: () -> Unit,
    onNavigateToAllTags: () -> Unit,
    onNavigateToAllStories: () -> Unit,
) {
    IndexServiceReadiness { indexService ->
        PageContainer {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = SMALL_PADDING.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                HomeHorizontalSpacer()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "...Kriper",
                        style = MaterialTheme.typography.h1
                    )
                }
                HomeHorizontalSpacer()
                IconicCardFull(
                    title = "Группы меток",
                    icon = Icons.Filled.Category,
                    description = "Проще выбрать что почитать",
                    onClick = onNavigateToTagGroups
                )
                HomeHorizontalSpacer()
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        IconicCardSmall(
                            title = "Все метки",
                            icon = Icons.Filled.Tag,
                            onClick = onNavigateToAllTags
                        )
                    }
                    Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                    Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        IconicCardSmall(
                            title = "Все истории",
                            icon = Icons.Filled.HistoryEdu,
                            onClick = onNavigateToAllStories
                        )
                    }
                }
                HomeHorizontalSpacer()
            }
        }
    }
}