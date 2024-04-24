package com.github.pokatomnik.kriper.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
private fun PageMetaUI(
    pageMeta: PageMeta,
    liked: Boolean,
    onClick: (PageMeta) -> Unit,
) {
    ShortDescriptionDialog(storyId = pageMeta.storyId) { showShortDescription ->
        StoryCardNavigationListItem(
            title = pageMeta.title,
            tags = pageMeta.tags,
            rating = pageMeta.rating,
            author = pageMeta.authorship,
            readingTimeMinutes = pageMeta.readingTimeMinutes,
            liked = liked,
            onClick = { onClick(pageMeta) },
            onLongClick = showShortDescription
        )
    }
}

enum class HideStoriesType {
    META_LIST,
    READ_STORIES
}

@Composable
fun PageMetaLazyList(
    pageMeta: List<PageMeta>,
    lazyListState: LazyListState = rememberLazyListState(),
    canAddAndRemoveFavorite: Boolean = false,
    hideStoriesType: HideStoriesType? = null,
    onPageMetaClick: (pageMeta: PageMeta) -> Unit,
) {
    val kriperDatabase = rememberKriperDatabase()
    val favoriteStoriesDAO = kriperDatabase.favoriteStoriesDAO()
    val historyDAO = kriperDatabase.historyDAO()

    val (hideReadStoriesPreferred) = rememberPreferences()
        .globalPreferences
        .hideReadStories
        .collectAsState()

    val favoritesMap = remember {
        mutableStateOf<Map<String, PageMeta>>(mapOf())
    }
    val readStoriesIds = remember {
        mutableStateOf<Set<String>>(setOf())
    }

    val actualPageMeta = remember(
        pageMeta,
        readStoriesIds.value,
        hideStoriesType,
        hideReadStoriesPreferred
    ) {
        if (hideStoriesType == HideStoriesType.META_LIST && hideReadStoriesPreferred) {
            pageMeta
                .filter { currentPageMeta ->
                    !readStoriesIds.value.contains(currentPageMeta.storyId)
                }
        } else pageMeta
    }

    IndexServiceReadiness { indexService ->
        LaunchedEffect(Unit) {
            launch {
                withContext(Dispatchers.IO + SupervisorJob()) {
                    favoritesMap.value = favoriteStoriesDAO
                        .getAllFavoriteIds()
                        .fold(mutableMapOf()) { acc, currentId ->
                            acc.apply {
                                indexService.content.getPageMetaByStoryId(currentId)?.let {
                                    this[currentId] = it
                                }
                            }
                        }
                }
            }
            launch {
                withContext(Dispatchers.IO + SupervisorJob()) {
                    readStoriesIds.value = historyDAO.getAllReadStoriesIdSet()
                }
            }
        }

        if (actualPageMeta.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LARGE_PADDING.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = when (hideStoriesType) {
                        HideStoriesType.READ_STORIES -> "Прочитанных пока нет"
                        HideStoriesType.META_LIST -> "Кажется тут ничего нет.\nВозможно Вы уже все прочитали:)"
                        null -> "Здесь ничего нет"
                    },
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyList(list = actualPageMeta, lazyListState = lazyListState) { index, pageMetaItem ->
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
                                    return if (favoritesMap.value.contains(pageMetaItem.storyId)) {
                                        Icons.Filled.Delete
                                    } else {
                                        Icons.Filled.Favorite
                                    }
                                }
                            override val contentDescription: String
                                get() {
                                    return if (favoritesMap.value.contains(pageMetaItem.storyId)) {
                                        "Удалить из избранного"
                                    } else {
                                        "Добавить в избранное"
                                    }
                                }

                            override suspend fun onSwipe(): suspend () -> Unit {
                                return {
                                    if (favoritesMap.value.contains(pageMetaItem.storyId)) {
                                        favoriteStoriesDAO.removeFromFavorites(pageMetaItem.storyId)
                                        favoritesMap.value = favoritesMap.value
                                            .toMutableMap()
                                            .apply { remove(pageMetaItem.storyId) }
                                    } else {
                                        favoriteStoriesDAO.addToFavorites(pageMetaItem.storyId)
                                        favoritesMap.value = favoritesMap.value
                                            .toMutableMap()
                                            .apply { put(pageMetaItem.storyId, pageMetaItem) }
                                    }
                                }
                            }
                        }
                    ) {
                        PageMetaUI(
                            pageMeta = pageMetaItem,
                            liked = favoritesMap.value.contains(pageMetaItem.storyId),
                            onClick = onPageMetaClick
                        )
                    }
                } else {
                    PageMetaUI(
                        pageMeta = pageMetaItem,
                        liked = favoritesMap.value.contains(pageMetaItem.storyId),
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
    }
}
