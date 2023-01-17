package com.github.pokatomnik.kriper.screens.story

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.github.pokatomnik.kriper.services.preferences.page.ColorsInfo

@Composable
fun ContentSurface(
    colorPresetState: MutableState<ColorsInfo>,
    content: @Composable () -> Unit,
) {
    val color = colorPresetState.value.backgroundColor ?: MaterialTheme.colors.surface
    val contentColor = colorPresetState.value.contentColor ?: contentColorFor(
        MaterialTheme.colors.surface
    )
    Surface(
        color = color,
        contentColor = contentColor,
        content = content
    )
}