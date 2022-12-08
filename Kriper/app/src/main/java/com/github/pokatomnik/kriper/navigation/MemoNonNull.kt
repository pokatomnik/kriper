package com.github.pokatomnik.kriper.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun <T : Any> RequireNonNull(nullableValue: T?, content: @Composable (T) -> Unit) {
    val (value, setValue) = remember {
        mutableStateOf(nullableValue)
    }

    LaunchedEffect(nullableValue) {
        if (nullableValue != null) setValue(nullableValue)
    }

    if (value != null) content(value)
}