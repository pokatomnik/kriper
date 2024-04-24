package com.github.pokatomnik.kriper.screens.selections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.services.index.Selections
import com.github.pokatomnik.kriper.ui.components.BottomSheet
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.widgets.HideStoriesType
import com.github.pokatomnik.kriper.ui.widgets.PageMetaLazyList
import com.github.pokatomnik.kriper.ui.widgets.sortingStateWithUI
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
fun makeParametrizedSelection(
    selectionTitle: String,
    getSelection: (Selections) -> Collection<PageMeta>,
): @Composable (
    onNavigateBack: () -> Unit,
    onNavigateToStoryById: (storyId: String) -> Unit,
) -> Unit {
    val component: @Composable (
        onNavigateBack: () -> Unit,
        onNavigateToStoryById: (storyId: String) -> Unit,
    ) -> Unit = @Composable { onNavigateBack, onNavigateToStoryById ->
        val coroutineScope = rememberCoroutineScope()

        IndexServiceReadiness { indexService ->
            val lazyListState = rememberLazyListState()
            val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
            val (sortingState, renderSortingOptions) = sortingStateWithUI {
                coroutineScope.launch { drawerState.close() }
                coroutineScope.launch { lazyListState.animateScrollToItem(0) }
            }
            val sortedPageMeta = remember(sortingState.value) {
                getSelection(indexService.content.selections)
                    .sortedWith { a, b -> sortingState.value.compare(a, b) }
            }

            BottomSheet(
                drawerState = drawerState,
                content = {
                    PageContainer(
                        priorButton = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                                pageMeta = sortedPageMeta,
                                lazyListState = lazyListState,
                                hideStoriesType = HideStoriesType.META_LIST,
                                onPageMetaClick = { onNavigateToStoryById(it.storyId) }
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

val GoldStories = makeParametrizedSelection("Золотой фонд") { it.gold }

val WeekTop = makeParametrizedSelection("Топ за неделю") { it.weekTop }

val MonthTop = makeParametrizedSelection("Топ за месяц") { it.monthTop }

val YearTop = makeParametrizedSelection("Топ за год") { it.yearTop }

val AllTheTimeTop = makeParametrizedSelection("Топ за все время") { it.allTheTimeTop }