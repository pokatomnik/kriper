package com.github.pokatomnik.kriper.screens.search

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.screens.history.TopBarSearchInput
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.services.index.SearchResults
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

const val PAGES_INDEX = 0
const val TAGS_INDEX = 1
const val TAG_GROUPS_INDEX = 2

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Search(
    onNavigateBack: () -> Unit,
    onNavigateToTagGroup: (tagGroupTitle: String) -> Unit,
    onNavigateToTag: (tagTitle: String) -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit,
) {
    val searchingState = remember { mutableStateOf(false) }
    val searchStringState = remember { mutableStateOf("") }
    val searchResultsState = remember { mutableStateOf<SearchResults?>(null) }
    val pagerState = rememberPagerState(PAGES_INDEX)

    val focusRequester = remember { FocusRequester() }
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    val requestFocus = {
        focusRequester.requestFocus()
        softwareKeyboardController?.show()
    }

    val freeFocus = {
        focusRequester.freeFocus()
        softwareKeyboardController?.hide()
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        requestFocus()
    }

    IndexServiceReadiness { indexService ->
        val doSearch = fun() {
            if (searchingState.value) return
            val strToSearch = searchStringState.value.trim()
            coroutineScope.launch {
                val searchResults = indexService.content.search(strToSearch)
                searchResultsState.value = searchResults
                searchingState.value = false

                freeFocus()

                if (searchResults.pageMeta.isNotEmpty()) {
                    pagerState.animateScrollToPage(PAGES_INDEX)
                } else if (searchResults.tagContentItems.isNotEmpty()) {
                    pagerState.animateScrollToPage(TAGS_INDEX)
                } else if (searchResults.tagGroups.isNotEmpty()) {
                    pagerState.animateScrollToPage(TAG_GROUPS_INDEX)
                } else {
                    pagerState.animateScrollToPage(PAGES_INDEX)
                }
            }
        }

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
                TopBarSearchInput(
                    searchTextState = searchStringState,
                    onSearchButtonPress = doSearch,
                    focusRequester = focusRequester
                )
            },
            trailingButton = {
                IconButton(onClick = doSearch) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Искать"
                    )
                }
            }
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.wrapContentWidth().height(48.dp),
                edgePadding = LARGE_PADDING.dp
            ) {
                Tab(
                    selected = pagerState.currentPage == PAGES_INDEX,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(PAGES_INDEX) }
                    },
                    text = { Text("Истории") }
                )
                Tab(
                    selected = pagerState.currentPage == TAGS_INDEX,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(TAGS_INDEX) }
                    },
                    text = { Text("Метки") }
                )
                Tab(
                    selected = pagerState.currentPage == TAG_GROUPS_INDEX,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(TAG_GROUPS_INDEX) }
                    },
                    text = { Text("Группы меток") }
                )
            }
            HorizontalPager(
                count = 4,
                state = pagerState,
            ) {
                if (it == PAGES_INDEX) {
                    PagesSearchResults(
                        pageMeta = searchResultsState.value?.pageMeta,
                        onNavigateToStory = onNavigateToStory
                    )
                }
                if (it == TAGS_INDEX) {
                    TagsSearchResults(
                        tagContentItems = searchResultsState.value?.tagContentItems,
                        onNavigateToTag = onNavigateToTag
                    )
                }
                if (it == TAG_GROUPS_INDEX) {
                    TagGroupSearchResults(
                        tagGroups = searchResultsState.value?.tagGroups,
                        onNavigateToTagGroup = onNavigateToTagGroup
                    )
                }
            }
        }
    }
}