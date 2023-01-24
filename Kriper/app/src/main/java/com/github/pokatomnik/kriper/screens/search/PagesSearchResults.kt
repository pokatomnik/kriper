package com.github.pokatomnik.kriper.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.ui.components.LazyList
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.widgets.StoryCardNavigationListItem

@Composable
fun PagesSearchResults(
    pageMeta: Collection<PageMeta>?,
    onNavigateToStory: (storyTitle: String) -> Unit,
) {
    if (pageMeta == null) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = SMALL_PADDING.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Тут будут найденные истории")
        }
    } else if (pageMeta.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = SMALL_PADDING.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Тут ничего не найдено")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SMALL_PADDING.dp)
        ) {
            LazyList(list = pageMeta.toList()) { index, pageMeta ->
                val isFirst = 0 == index

                if (isFirst) {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(SMALL_PADDING.dp))
                }
                StoryCardNavigationListItem(
                    title = pageMeta.title,
                    tags = pageMeta.tags,
                    rating = pageMeta.rating,
                    author = pageMeta.authorship,
                    readingTimeMinutes = pageMeta.readingTimeMinutes,
                    onClick = { onNavigateToStory(pageMeta.title) }
                )
                Spacer(
                    modifier = Modifier.fillMaxWidth().height(SMALL_PADDING.dp)
                )
            }
        }
    }
}