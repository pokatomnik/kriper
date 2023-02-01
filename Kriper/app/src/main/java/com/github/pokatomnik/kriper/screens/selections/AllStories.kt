package com.github.pokatomnik.kriper.screens.selections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.github.pokatomnik.kriper.services.index.Selections
import com.github.pokatomnik.kriper.ui.components.*
import com.github.pokatomnik.kriper.ui.widgets.PageMetaLazyList
import com.github.pokatomnik.kriper.ui.widgets.sortingStateWithUI
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
private fun makeParametrizedSelection(
    selectionTitle: String,
    getSelection: (Selections) -> Collection<String>,
): @Composable (
    onNavigateBack: () -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit,
) -> Unit {
    val component: @Composable (
        onNavigateBack: () -> Unit,
        onNavigateToStory: (storyTitle: String) -> Unit,
    ) -> Unit = @Composable { onNavigateBack, onNavigateToStory ->
        val coroutineScope = rememberCoroutineScope()

        IndexServiceReadiness { indexService ->
            val lazyListState = rememberLazyListState()
            val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
            val (sortingState, renderSortingOptions) = sortingStateWithUI {
                coroutineScope.launch { drawerState.close() }
                coroutineScope.launch { lazyListState.animateScrollToItem(0) }
            }
            val allPageMeta = remember(sortingState.value) {
                getSelection(indexService.content.selections)
                    .fold(mutableListOf<PageMeta>()) { acc, currentPageTitle ->
                        acc.apply {
                            indexService.content.getPageMetaByName(currentPageTitle)?.let { add(it) }
                        }
                    }.sortedWith { a, b -> sortingState.value.compare(a, b) }
            }

            BottomSheet(
                drawerState = drawerState,
                content = {
                    PageContainer(
                        priorButton = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Назад"
                                )
                            }
                        },
                        header = {
                            PageTitle(title = selectionTitle)
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
                            PageMetaLazyList(
                                pageMeta = allPageMeta,
                                lazyListState = lazyListState,
                                onPageMetaClick = { onNavigateToStory(it.title) }
                            )
                        }
                    }
                },
                drawerContent = { renderSortingOptions() }
            )
        }
    }

    return component
}

val AllStories = makeParametrizedSelection("Все истории") { it.allStoryTitles }

val ShortStories = makeParametrizedSelection("Короткие истоии") { it.shortAndMostUpVoted }

val LongStories = makeParametrizedSelection("Длинные истории") { it.longAndMostUpVoted }

val NewStories = makeParametrizedSelection("Новые истории") { it.new }