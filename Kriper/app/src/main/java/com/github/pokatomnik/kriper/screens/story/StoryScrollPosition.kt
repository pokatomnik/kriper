package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import kotlinx.coroutines.launch

@Composable
fun InitialStoryScrollPosition(
    storyId: String,
    content: @Composable (scrollPosition: Int) -> Unit,
) {
    val historyDAO = rememberKriperDatabase().historyDAO()
    val (scrollPosition, setScrollPosition) = remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(storyId) {
        setScrollPosition(null)
        launch {
            val historyItem = historyDAO.getById(storyId)
            setScrollPosition(historyItem?.scrollPosition ?: 0)
        }
    }

    scrollPosition?.let {
        content(scrollPosition)
    }
}

@Composable
fun StoryScrollPosition(
    storyId: String,
    content: @Composable (scrollState: ScrollState) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val historyDAO = rememberKriperDatabase().historyDAO()
    val scrollState = rememberScrollState()

    InitialStoryScrollPosition(storyId = storyId) { scrollPosition ->
        LaunchedEffect(scrollPosition) {
            scrollState.animateScrollTo(scrollPosition)
        }

        if (scrollState.isScrollInProgress) {
            DisposableEffect(Unit) {
                onDispose {
                    coroutineScope.launch {
                        historyDAO.setScrollPosition(storyId, scrollState.value)
                    }
                }
            }
        }

        content(scrollState)
    }
}