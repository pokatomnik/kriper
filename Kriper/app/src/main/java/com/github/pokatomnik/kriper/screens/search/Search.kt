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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.screens.history.TopBarSearchInput
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.services.index.SearchResults
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.makeToast
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

private const val REQUIRED_CHARS_NUMBER_TO_SEARCH = 4

private const val PAGES_INDEX = 0
private const val TAGS_INDEX = 1
private const val TAG_GROUPS_INDEX = 2

private fun SearchResults.getTabIndexToScrollTo(): Int {
    return if (pageMeta.isNotEmpty()) {
        PAGES_INDEX
    } else if (tagContentItems.isNotEmpty()) {
        TAGS_INDEX
    } else if (tagGroups.isNotEmpty()) {
        TAG_GROUPS_INDEX
    } else {
        PAGES_INDEX
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Search(
    onNavigateBack: () -> Unit,
    onNavigateToTagGroup: (tagGroupTitle: String) -> Unit,
    onNavigateToTag: (tagTitle: String) -> Unit,
    onNavigateToStoryById: (storyId: String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val searchPreferences = rememberPreferences().searchPreferences
    val (fullTextSearch) = searchPreferences.fullTextSearch.collectPreferFullTextSearchAsState()
    val searchStringState = searchPreferences.let {
        remember {
            mutableStateOf(
                TextFieldValue(
                    text = it.searchValue,
                    selection = TextRange(it.searchValue.length)
                )
            )
        }.apply {
            LaunchedEffect(value) {
                it.searchValue = value.text
            }
        }
    }

    val searchingState = remember { mutableStateOf(false) }
    val searchResultsState = remember { mutableStateOf<SearchResults?>(null) }
    val pagerState = rememberPagerState(PAGES_INDEX)

    val toast = makeToast()

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

    LaunchedEffect(Unit) {
        requestFocus()
    }

    IndexServiceReadiness { indexService ->
        val autoSearchIfStringIsNotEmpty = fun() {
            val strToSearch = searchStringState.value.text.trim()
            if (strToSearch.isEmpty()) return
            searchingState.value = true
            coroutineScope.launch {
                val searchResults = indexService.content.search(strToSearch, fullTextSearch)
                searchResultsState.value = searchResults
                searchingState.value = false
                pagerState.animateScrollToPage(
                    searchResults.getTabIndexToScrollTo()
                )
            }
        }

        val requestSearch = fun() {
            if (searchingState.value) return
            val strToSearch = searchStringState.value.text.trim()
            if (strToSearch.length < REQUIRED_CHARS_NUMBER_TO_SEARCH) {
                toast("Введите больше 4 букв")
                return
            }
            searchingState.value = true
            coroutineScope.launch {
                val searchResults = indexService.content.search(strToSearch, fullTextSearch)
                searchResultsState.value = searchResults
                searchingState.value = false

                freeFocus()

                pagerState.animateScrollToPage(
                    searchResults.getTabIndexToScrollTo()
                )
            }
        }

        LaunchedEffect(Unit) {
            autoSearchIfStringIsNotEmpty()
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
                    textFieldValue = searchStringState.value,
                    onTextFieldValueChange = { searchStringState.value = it },
                    onSearchButtonPress = requestSearch,
                    focusRequester = focusRequester
                )
            },
            trailingButton = {
                IconButton(onClick = requestSearch) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Искать"
                    )
                }
            }
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(48.dp),
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
                        isSearching = searchingState.value,
                        pageMeta = searchResultsState.value?.pageMeta,
                        onNavigateToStoryById = onNavigateToStoryById
                    )
                }
                if (it == TAGS_INDEX) {
                    TagsSearchResults(
                        isSearching = searchingState.value,
                        tagContentItems = searchResultsState.value?.tagContentItems,
                        onNavigateToTag = onNavigateToTag
                    )
                }
                if (it == TAG_GROUPS_INDEX) {
                    TagGroupSearchResults(
                        isSearching = searchingState.value,
                        tagGroups = searchResultsState.value?.tagGroups,
                        onNavigateToTagGroup = onNavigateToTagGroup
                    )
                }
            }
        }
    }
}