package com.github.pokatomnik.kriper.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.services.preferences.global.ThemeIdentifier
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
private fun KriperThemeInternal(
    content: @Composable () -> Unit
) {
    val themeState = rememberPreferences()
        .globalPreferences
        .themeSelection
        .collectAsState()

    val colors = when (themeState.value) {
        ThemeIdentifier.LIGHT -> LightColorPalette
        ThemeIdentifier.DARK -> DarkColorPalette
        else -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun KriperTheme(
    content: @Composable () -> Unit
) {
    val preferences = rememberPreferences().globalPreferences.themeSelection
    preferences.Provider {
        KriperThemeInternal {
            content()
        }
    }
}