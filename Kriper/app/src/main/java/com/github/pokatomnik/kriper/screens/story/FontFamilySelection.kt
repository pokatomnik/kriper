package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.preferences.page.FontInfo
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.SelectableRowRadio

@Composable
fun FontFamilySelection(
    fontInfoState: MutableState<FontInfo>
) {
    val availableFontInfos = rememberPreferences()
        .pagePreferences
        .storyContentFontFamily
        .availableFontInfos

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LARGE_PADDING.dp)
        ) {
            Text(
                text = "Выбор шрифта",
                fontWeight = FontWeight.Bold
            )
        }
        availableFontInfos.forEach { (fontInfoId, fontInfo) ->
            SelectableRowRadio(
                selected = fontInfoId == fontInfoState.value.id,
                onClick = { fontInfoState.value = fontInfo }
            ) {
                Text(
                    text = fontInfo.title,
                    fontFamily = fontInfo.fontResourceId?.let {
                        FontFamily(
                            Font(
                                resId = fontInfo.fontResourceId,
                                weight = FontWeight.Normal,
                                style = FontStyle.Normal
                            )
                        )
                    }
                )
            }
        }
    }
}