package com.github.pokatomnik.kriper.services.preferences.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.pokatomnik.kriper.services.preferences.PreferencesBooleanValue

class FullTextSearchPreferences(private val preferencesValue: PreferencesBooleanValue) {
    @Composable
    fun collectPreferFullTextSearchAsState(): MutableState<Boolean> {
        val (fullTextSearch, setFullTextSearch) = remember {
            mutableStateOf(
                preferencesValue.read(false)
            )
        }

        return object : MutableState<Boolean> {
            override var value: Boolean
                get() = fullTextSearch
                set(value) {
                    setFullTextSearch(value)
                    preferencesValue.write(value)
                }

            override fun component1(): Boolean {
                return fullTextSearch
            }

            override fun component2(): (Boolean) -> Unit {
                return { value ->
                    setFullTextSearch(value)
                    preferencesValue.write(value)
                }
            }

        }
    }
}