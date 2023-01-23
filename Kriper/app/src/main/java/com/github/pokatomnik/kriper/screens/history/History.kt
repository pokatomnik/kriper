package com.github.pokatomnik.kriper.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.*
import com.github.pokatomnik.kriper.ui.widgets.StoryCardNavigationListItem

@Composable
fun History(
    onNavigateBack: () -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit,
) {
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
            PageTitle(title = "Хронология")
        }
    ) {
        HistoryItems { pageMeta ->
            if (pageMeta.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(SMALL_PADDING.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Тут пока пусто")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = SMALL_PADDING.dp)
                ) {
                    LazyList(list = pageMeta) { index, currentPageMeta ->
                        val isFirst = 0 == index

                        if (isFirst) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(SMALL_PADDING.dp)
                            )
                        }
                        StoryCardNavigationListItem(
                            title = currentPageMeta.title,
                            tags = currentPageMeta.tags,
                            rating = currentPageMeta.rating,
                            author = currentPageMeta.authorship,
                            readingTimeMinutes = currentPageMeta.readingTimeMinutes,
                            onClick = { onNavigateToStory(currentPageMeta.title) }
                        )
                        Spacer(modifier = Modifier.fillMaxWidth().height(SMALL_PADDING.dp))
                    }
                }
            }
        }
    }
}