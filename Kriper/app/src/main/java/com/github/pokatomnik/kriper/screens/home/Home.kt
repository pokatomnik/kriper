package com.github.pokatomnik.kriper.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Tag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import kotlinx.coroutines.launch

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
    onNavigateToHistory: () -> Unit,
) {
    val animatedTitleTranslateX = remember { Animatable(-100f) }
    val animatedTitleAlpha = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        launch {
            animatedTitleTranslateX.animateTo(
                targetValue = 0f,
                animationSpec = tween(2000)
            )
        }
        launch {
            animatedTitleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(2000)
            )
        }
    }

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
                        style = MaterialTheme.typography.h1,
                        modifier = Modifier.graphicsLayer {
                            translationX = animatedTitleTranslateX.value
                            alpha = animatedTitleAlpha.value
                        }
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
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)) {
                        IconicCardSmall(
                            title = "Все метки",
                            icon = Icons.Filled.Tag,
                            onClick = onNavigateToAllTags
                        )
                    }
                    Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        IconicCardSmall(
                            title = "Все истории",
                            icon = Icons.Filled.HistoryEdu,
                            onClick = onNavigateToAllStories
                        )
                    }
                }
                HomeHorizontalSpacer()
                IconicCardFull(
                    title = "Хронология",
                    icon = Icons.Filled.History,
                    description = "Прочитано ранее",
                    onClick = onNavigateToHistory
                )
                HomeHorizontalSpacer()
            }
        }
    }
}