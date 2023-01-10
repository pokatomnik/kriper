package com.github.pokatomnik.kriper.screens.allstories

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.*
import com.github.pokatomnik.kriper.ui.components.PageTitle

@Composable
fun AllStories(
    onNavigateBack: () -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit,
) {
    IndexServiceReadiness { indexService ->
        val allStoryTitles = indexService.content.allStoryTitles.toList()

        PageContainer(
            priorButton = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад на главную"
                    )
                }
            },
            header = {
                PageTitle(title = "Все истории")
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(SMALL_PADDING.dp)
            ) {
                LazyList(list = allStoryTitles) { index, pageTitle ->
                    val isFirst = 0 == index
                    val pageMeta = indexService.content.getPageMetaByName(pageTitle)

                    if (isFirst) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(SMALL_PADDING.dp)
                        )
                    }
                    StoryCardNavigationListItem(
                        title = pageTitle,
                        tags = pageMeta?.tags ?: listOf(),
                        rating = pageMeta?.rating ?: 0,
                        author = pageMeta?.authorNickname ?: "Автор не указан",
                        readingTimeMinutes = pageMeta?.readingTimeMinutes ?: 0f,
                        onClick = { onNavigateToStory(pageTitle) }
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(SMALL_PADDING.dp)
                    )
                }
            }
        }
    }
}