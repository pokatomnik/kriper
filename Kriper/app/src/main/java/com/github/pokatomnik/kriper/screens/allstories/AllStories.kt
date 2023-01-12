package com.github.pokatomnik.kriper.screens.allstories

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.*
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.widgets.StoryCardNavigationListItem
import com.github.pokatomnik.kriper.ui.widgets.sortingStateWithUI
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllStories(
    onNavigateBack: () -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    IndexServiceReadiness { indexService ->
        val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
        val (sortingState, renderSortingOptions) = sortingStateWithUI {
            coroutineScope.launch { drawerState.close() }
        }
        val allPageMeta = remember(sortingState.value) {
            indexService.content.allStoryTitles.fold(mutableListOf<PageMeta>()) { acc, currentPageTitle ->
                acc.apply {
                    indexService.content.getPageMetaByName(currentPageTitle)?.let { add(it) }
                }
            }.sortedWith { a, b -> sortingState.value.sort(a, b) }
        }

        BottomSheet(
            drawerState = drawerState,
            content = {
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
                    },
                    trailingButton = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch { drawerState.open() }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = "Сортировка"
                            )
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = SMALL_PADDING.dp)
                    ) {
                        LazyList(list = allPageMeta) { index, pageMeta ->
                            val isFirst = 0 == index

                            if (isFirst) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(SMALL_PADDING.dp)
                                )
                            }
                            StoryCardNavigationListItem(
                                title = pageMeta.title,
                                tags = pageMeta.tags,
                                rating = pageMeta.rating,
                                author = pageMeta.authorNickname,
                                readingTimeMinutes = pageMeta.readingTimeMinutes,
                                onClick = { onNavigateToStory(pageMeta.title) }
                            )
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(SMALL_PADDING.dp)
                            )
                        }
                    }
                }
            },
            drawerContent = { renderSortingOptions() }
        )
    }
}