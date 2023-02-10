package com.github.pokatomnik.kriper.screens.allstoriesbyauthor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.BottomSheet
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.widgets.PageMetaLazyList
import com.github.pokatomnik.kriper.ui.widgets.sortingStateWithUI
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllStoriesByAuthorInternal(
    authorRealName: String,
    stories: Collection<PageMeta>,
    onNavigateBack: () -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val lazyListState = rememberLazyListState()
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val (sortingState, renderSortingOptions) = sortingStateWithUI {
        coroutineScope.launch { drawerState.close() }
        coroutineScope.launch { lazyListState.animateScrollToItem(0) }
    }
    val pageMeta = remember(stories, sortingState.value) {
        stories.sortedWith { a, b -> sortingState.value.compare(a, b) }
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
                    PageTitle(title = authorRealName)
                },
                trailingButton = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch { drawerState.open() }
                        }
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
                        pageMeta = pageMeta,
                        lazyListState = lazyListState,
                        onPageMetaClick = { onNavigateToStory(it.title) }
                    )
                }
            }
        },
        drawerContent = { renderSortingOptions() }
    )
}

@Composable
fun AllStoriesByAuthor(
    authorRealName: String,
    onNavigateBack: () -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit,
) {
    IndexServiceReadiness { indexService ->
        val storiesByAuthorState = remember { mutableStateOf(listOf<PageMeta>()) }

        LaunchedEffect(authorRealName) {
            launch {
                storiesByAuthorState.value = indexService.content
                    .getAllStoriesByAuthor(authorRealName)
                    .toList()
            }
        }
        
        AllStoriesByAuthorInternal(
            authorRealName = authorRealName,
            stories = storiesByAuthorState.value,
            onNavigateBack = onNavigateBack,
            onNavigateToStory = onNavigateToStory
        )
    }
}
