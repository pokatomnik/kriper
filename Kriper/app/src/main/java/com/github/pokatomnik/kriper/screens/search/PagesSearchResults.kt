package com.github.pokatomnik.kriper.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.widgets.PageMetaLazyList

@Composable
fun PagesSearchResults(
    isSearching: Boolean,
    pageMeta: Collection<PageMeta>?,
    onNavigateToStoryById: (storyId: String) -> Unit,
) {
    if (isSearching) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SMALL_PADDING.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else if (pageMeta == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SMALL_PADDING.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Тут будут найденные истории")
        }
    } else if (pageMeta.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SMALL_PADDING.dp),
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
            PageMetaLazyList(
                pageMeta = pageMeta.toList(),
                canHideReadStories = false,
                onPageMetaClick = { onNavigateToStoryById(it.storyId) }
            )
        }
    }
}