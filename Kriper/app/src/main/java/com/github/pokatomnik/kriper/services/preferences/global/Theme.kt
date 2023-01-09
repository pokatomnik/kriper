package com.github.pokatomnik.kriper.services.preferences.global

import androidx.compose.runtime.*
import com.github.pokatomnik.kriper.services.preferences.PreferencesStringValue

enum class ThemeIdentifier {
    AUTO,
    LIGHT,
    DARK
}

class Theme(private val preferencesValue: PreferencesStringValue) {
    private val localThemeIdentifier = compositionLocalOf<MutableState<ThemeIdentifier>> {
        object : MutableState<ThemeIdentifier> {
            override var value: ThemeIdentifier
                get() = ThemeIdentifier.AUTO
                set(_) {}
            override fun component1(): ThemeIdentifier {
                return ThemeIdentifier.AUTO
            }
            override fun component2(): (ThemeIdentifier) -> Unit {
                return {}
            }
        }
    }

    @Composable
    fun Provider(
        content: @Composable (state: MutableState<ThemeIdentifier>) -> Unit
    ) {
        val state = remember {
            val savedState = preferencesValue.read(defaultIdentifier)
            mutableStateOf(
                when (savedState) {
                    "LIGHT" -> ThemeIdentifier.LIGHT
                    "DARK" -> ThemeIdentifier.DARK
                    else -> ThemeIdentifier.AUTO
                }
            )
        }

        CompositionLocalProvider(localThemeIdentifier provides state) {
            content(state)
        }
    }

    @Composable
    fun collectAsState(): MutableState<ThemeIdentifier> {
        val themeIdentifierState = localThemeIdentifier.current

        LaunchedEffect(themeIdentifierState.value) {
            preferencesValue.write(
                when (themeIdentifierState.value) {
                    ThemeIdentifier.AUTO -> defaultIdentifier
                    ThemeIdentifier.LIGHT -> "LIGHT"
                    ThemeIdentifier.DARK -> "DARK"
                }
            )
        }

        return themeIdentifierState
    }

    companion object {
        const val defaultIdentifier = "AUTO"
    }
}