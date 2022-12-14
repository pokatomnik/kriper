package com.github.pokatomnik.kriper.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable

@Composable
fun <T : Any> LazyList(list: List<T>, renderItem: @Composable (T) -> Unit) {
    val lazyListState = rememberLazyListState()
    LazyColumn(state = lazyListState) {
        items(list) {
            renderItem(it)
        }
    }
}