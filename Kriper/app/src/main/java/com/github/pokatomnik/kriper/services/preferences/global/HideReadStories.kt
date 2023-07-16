package com.github.pokatomnik.kriper.services.preferences.global

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.pokatomnik.kriper.services.preferences.PreferencesBooleanValue

class HideReadStories(private val preferencesValue: PreferencesBooleanValue) {
    @Composable
    fun collectAsState(): MutableState<Boolean> {
        val (hideReadStories, setHideReadStories) = remember {
            mutableStateOf(
                preferencesValue.read(false)
            )
        }

        return object : MutableState<Boolean> {
            override var value: Boolean
                get() = hideReadStories
                set(value) {
                    setHideReadStories(value)
                    preferencesValue.write(value)
                }

            override fun component1(): Boolean {
                return hideReadStories
            }

            override fun component2(): (Boolean) -> Unit {
                return { value ->
                    setHideReadStories(value)
                    preferencesValue.write(value)
                }
            }
        }
    }
}