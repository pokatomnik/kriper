package com.github.pokatomnik.kriper.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalAppDarkTheme = compositionLocalOf { false }

@Composable
fun isLocalAppDarkThemeEnabled(): Boolean {
    return LocalAppDarkTheme.current
}