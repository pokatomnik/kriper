package com.github.pokatomnik.kriper.screens.settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.components.SelectableRowCheckbox

@Composable
fun HideReadStoriesSection() {
    val (hideReadStories, setHideReadStories) = rememberPreferences()
        .globalPreferences
        .hideReadStories
        .collectAsState()

    SectionTitle(title = "Список историй")
    SelectableRowCheckbox(
        checked = hideReadStories,
        onClick = { setHideReadStories(!hideReadStories) }
    ) {
        Text("Скрывать прочитанные")
    }
}