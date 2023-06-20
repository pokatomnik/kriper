package com.github.pokatomnik.kriper.screens.favoritestories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.widgets.PageMetaLazyList

@Composable
fun FavoriteStoriesContent(
    swipeRefreshKey: Int,
    onNavigateToStoryById: (storyId: String) -> Unit
) {
    IndexServiceReadiness { indexService ->
        val favoriteStoriesDAO = rememberKriperDatabase().favoriteStoriesDAO()

        val pagesMetaList = remember { mutableStateOf<List<PageMeta>?>(null) }

        LaunchedEffect(swipeRefreshKey) {
            pagesMetaList.value = favoriteStoriesDAO
                .getAllFavoriteIds()
                .fold(mutableListOf()) { acc, currentId ->
                    acc.apply {
                        indexService.content.getPageMetaByStoryId(currentId)?.let { add(it) }
                    }
                }
        }

        pagesMetaList.value?.let { pagesMeta ->
            if (pagesMeta.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = LARGE_PADDING.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "В Избранном пока пусто. Чтобы добавить в избранное на странице истории, тапните по тексту 2 раза",
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = SMALL_PADDING.dp)
                ) {
                    PageMetaLazyList(
                        pageMeta = pagesMeta,
                        canAddAndRemoveFavorite = true,
                        onPageMetaClick = { onNavigateToStoryById(it.storyId) }
                    )
                }
            }
        }
    }
}