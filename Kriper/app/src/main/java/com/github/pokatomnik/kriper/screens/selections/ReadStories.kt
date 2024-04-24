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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.BottomSheet
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.widgets.HideStoriesType
import com.github.pokatomnik.kriper.ui.widgets.PageMetaLazyList
import com.github.pokatomnik.kriper.ui.widgets.sortingStateWithUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ReadStories(
    selectionTitle: String,
    onNavigateBack: () -> Unit,
    onNavigateToStoryById: (storyId: String) -> Unit,
) {
    val historyDAO = rememberKriperDatabase().historyDAO()
    val readStoryIdsState = remember { mutableStateOf<Set<String>?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO + SupervisorJob()) {
            val readStoriesIds = historyDAO.getAllReadStoriesIdSet()
            readStoryIdsState.value = readStoriesIds
        }
    }

    IndexServiceReadiness { indexService ->
        val pageMeta: List<PageMeta>? = remember (readStoryIdsState.value) {
            readStoryIdsState.value?.let { readStoriesIds ->
                readStoriesIds.fold(mutableListOf()) { acc, currentStoryId ->
                    acc.apply {
                        indexService.content.getPageMetaByStoryId(currentStoryId)?.let {
                            add(it)
                        }
                    }
                }
            }
        }
        if (pageMeta != null) {
            ReadStoriesInternal(
                selectionTitle = selectionTitle,
                pageMeta = pageMeta,
                onNavigateBack = onNavigateBack,
                onNavigateToStoryById = onNavigateToStoryById
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ReadStoriesInternal(
    selectionTitle: String,
    pageMeta: List<PageMeta>,
    onNavigateBack: () -> Unit,
    onNavigateToStoryById: (storyId: String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val lazyListState = rememberLazyListState()
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val (sortingState, renderSortingOptions) = sortingStateWithUI {
        coroutineScope.launch { drawerState.close() }
        coroutineScope.launch { lazyListState.animateScrollToItem(0) }
    }
    val sortedPageMeta = remember(sortingState.value, pageMeta) {
        pageMeta.sortedWith { a, b -> sortingState.value.compare(a, b) }
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
                        hideStoriesType = HideStoriesType.READ_STORIES,
                        onPageMetaClick = { onNavigateToStoryById(it.storyId) }
                    )
                }
            }
        },
        drawerContent = { renderSortingOptions() }
    )
}