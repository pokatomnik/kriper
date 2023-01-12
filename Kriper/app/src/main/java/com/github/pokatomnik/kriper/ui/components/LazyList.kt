package com.github.pokatomnik.kriper.ui.components

import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable

@Composable
fun <T : Any> LazyList(
    list: List<T>,
    lazyListState: LazyListState = rememberLazyListState(),
    renderItem: @Composable (index: Int, item: T) -> Unit
) {
    LazyColumn(state = lazyListState) {
        itemsIndexed(list) { index, item ->
            renderItem(index, item)
        }
    }
}