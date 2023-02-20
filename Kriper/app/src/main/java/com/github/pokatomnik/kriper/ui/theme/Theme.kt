package com.github.pokatomnik.kriper.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.github.pokatomnik.kriper.services.preferences.global.ThemeIdentifier
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = DarkPrimary,
    secondary = DarkSecondary
)

private val LightColorPalette = lightColors(
    primary = LightPrimary,
    secondary = LightSecondary
)

@Composable
private fun KriperThemeInternal(
    content: @Composable () -> Unit
) {
    val systemUIController = rememberSystemUiController()
    val themeState = rememberPreferences()
        .globalPreferences
        .themeSelection
        .collectAsState()

    val colors = when (themeState.value) {
        ThemeIdentifier.LIGHT -> LightColorPalette
        ThemeIdentifier.DARK -> DarkColorPalette
        else -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
    }

    val isDarkThemeSelectedByUserOrDefinedBySystem = when (themeState.value) {
        ThemeIdentifier.LIGHT -> false
        ThemeIdentifier.DARK -> true
        else -> isSystemInDarkTheme()
    }

    systemUIController.setSystemBarsColor(colors.primarySurface)

    CompositionLocalProvider(LocalAppDarkTheme provides isDarkThemeSelectedByUserOrDefinedBySystem) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
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