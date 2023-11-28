package com.github.pokatomnik.kriper.screens.settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.components.SelectableRowCheckbox

@Composable
fun SearchPreferencesSection() {
    val (fullTextSearch, setFullTextSearch) = rememberPreferences()
        .searchPreferences
        .fullTextSearch
        .collectPreferFullTextSearchAsState()
    SectionTitle(title = "Настройки поиска")
    SelectableRowCheckbox(
        checked = fullTextSearch,
        onClick = { setFullTextSearch(!fullTextSearch) }
    ) {
        Text("Искать по тексту историй")
    }
}