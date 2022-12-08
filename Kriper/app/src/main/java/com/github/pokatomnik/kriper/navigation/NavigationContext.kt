package com.github.pokatomnik.kriper.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

private val LocalNavigation = compositionLocalOf<Navigation?> { null }

@Composable
fun NavigationProvider(
    content: @Composable () -> Unit
) {
    val navigation = rememberNavigationInit()
    CompositionLocalProvider(LocalNavigation provides navigation) {
        content()
    }
}

@Composable
fun rememberNavigation(): Navigation {
    return LocalNavigation.current ?: throw Error("NavigationProvider is missing")
}