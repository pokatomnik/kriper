package com.github.pokatomnik.kriper.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@Composable
fun rememberScrollStateAndSubscribe(
    initial: Int = 0,
    onScroll: (scrollValue: Int, prevScrollValue: Int) -> Unit
): ScrollState {
    var prevScroll by remember { mutableStateOf(initial) }
    val scrollState = rememberScrollState(initial)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(scrollState.value) {
        coroutineScope.launch {
            onScroll(scrollState.value, prevScroll)
            prevScroll = scrollState.value
        }
    }

    return scrollState
}