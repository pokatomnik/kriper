package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.preferences.page.ColorsInfo
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.components.SelectableRowRadio

@Composable
fun ContentColorSelection(
    colorPresetState: MutableState<ColorsInfo>
) {
    val availableColorPresets = rememberPreferences()
        .pagePreferences
        .storyContentColorPreset
        .availableColorPresets

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LARGE_PADDING.dp)
        ) {
            Text(
                text = "Выбор цветов",
                fontWeight = FontWeight.Bold
            )
        }
        availableColorPresets.forEach { (colorPresetId, colorPreset) ->
            SelectableRowRadio(
                selected = colorPresetId == colorPresetState.value.id,
                onClick = { colorPresetState.value = colorPreset }
            ) {
                Text(
                    text = colorPreset.title,
                    modifier = Modifier
                        .background(
                            colorPreset.backgroundColor ?: MaterialTheme.colors.surface
                        )
                        .padding(horizontal = SMALL_PADDING.dp),
                    color = colorPreset.contentColor ?: contentColorFor(
                        MaterialTheme.colors.surface
                    )
                )
            }
        }
    }
}