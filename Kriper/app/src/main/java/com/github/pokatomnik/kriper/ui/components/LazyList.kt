package com.github.pokatomnik.kriper.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T : Any> LazyList(
    list: List<T>,
    lazyListState: LazyListState = rememberLazyListState(),
    renderItem: @Composable (index: Int, item: T) -> Unit
) {
    LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
        itemsIndexed(list) { index, item ->
            renderItem(index, item)
        }
    }
}