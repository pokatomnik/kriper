package com.github.pokatomnik.kriper.ui.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import com.github.pokatomnik.kriper.ui.components.HorizontalSwipeableRow
import com.github.pokatomnik.kriper.ui.components.LazyList
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.components.SwipeableActionParams

@Composable
private fun PageMetaUI(
    pageMeta: PageMeta,
    liked: Boolean,
    onClick: (PageMeta) -> Unit,
) {
    StoryCardNavigationListItem(
        title = pageMeta.title,
        tags = pageMeta.tags,
        rating = pageMeta.rating,
        author = pageMeta.authorship,
        readingTimeMinutes = pageMeta.readingTimeMinutes,
        liked = liked,
        onClick = { onClick(pageMeta) }
    )
}

@Composable
fun PageMetaLazyList(
    pageMeta: List<PageMeta>,
    lazyListState: LazyListState = rememberLazyListState(),
    canAddAndRemoveFavorite: Boolean = false,
    onPageMetaClick: (pageMeta: PageMeta) -> Unit,
) {
    val favoriteStoriesDAO = rememberKriperDatabase().favoriteStoriesDAO()
    val favoriteTitlesState = remember { mutableStateOf<Set<String>>(setOf()) }

    LaunchedEffect(Unit) {
        favoriteTitlesState.value = favoriteStoriesDAO.getAllFavoriteTitles().toSet()
    }

    LazyList(list = pageMeta, lazyListState = lazyListState) { index, pageMetaItem ->
        val isFirst = 0 == index

        if (isFirst) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SMALL_PADDING.dp)
            )
        }
        if (canAddAndRemoveFavorite) {
            HorizontalSwipeableRow(
                leftSwipeableHandler = object : SwipeableActionParams {
                    override val icon: ImageVector
                        get() {
                            return if (favoriteTitlesState.value.contains(pageMetaItem.title)) {
                                Icons.Filled.Delete
                            } else {
                                Icons.Filled.Favorite
                            }
                        }
                    override val contentDescription: String
                        get() {
                            return if (favoriteTitlesState.value.contains(pageMetaItem.title)) {
                                "Удалить из избранного"
                            } else {
                                "Добавить в избранное"
                            }
                        }
                    override suspend fun onSwipe(): suspend () -> Unit {
                        return {
                            if (favoriteTitlesState.value.contains(pageMetaItem.title)) {
                                favoriteStoriesDAO.removeFromFavorites(pageMetaItem.title)
                                favoriteTitlesState.value = favoriteTitlesState.value
                                    .toMutableSet()
                                    .apply { remove(pageMetaItem.title) }
                            } else {
                                favoriteStoriesDAO.addToFavorites(pageMetaItem.title)
                                favoriteTitlesState.value = favoriteTitlesState.value
                                    .toMutableSet()
                                    .apply { add(pageMetaItem.title) }
                            }
                        }
                    }
                }
            ) {
                PageMetaUI(
                    pageMeta = pageMetaItem,
                    liked = favoriteTitlesState.value.contains(pageMetaItem.title),
                    onClick = onPageMetaClick
                )
            }
        } else {
            PageMetaUI(
                pageMeta = pageMetaItem,
                liked = favoriteTitlesState.value.contains(pageMetaItem.title),
                onClick = onPageMetaClick
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(SMALL_PADDING.dp)
        )
    }
}
