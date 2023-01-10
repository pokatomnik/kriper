package com.github.pokatomnik.kriper.screens.settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.services.preferences.global.ThemeIdentifier
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.components.SelectableRow

@Composable
fun ThemeSection() {
    val (themeIdentifier, setThemeIdentifier) = rememberPreferences()
        .globalPreferences
        .themeSelection
        .collectAsState()

    ThemeSectionTitle(title = "Тема")
    SelectableRow(
        selected = themeIdentifier == ThemeIdentifier.AUTO,
        onClick = { setThemeIdentifier(ThemeIdentifier.AUTO) }
    ) {
        Text("Как в системе")
    }
    SelectableRow(
        selected = themeIdentifier == ThemeIdentifier.LIGHT,
        onClick = { setThemeIdentifier(ThemeIdentifier.LIGHT) }
    ) {
        Text("Светлая")
    }
    SelectableRow(
        selected = themeIdentifier == ThemeIdentifier.DARK,
        onClick = { setThemeIdentifier(ThemeIdentifier.DARK) }
    ) {
        Text("Темная")
    }
}