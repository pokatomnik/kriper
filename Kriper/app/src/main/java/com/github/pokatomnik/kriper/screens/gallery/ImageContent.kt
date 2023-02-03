package com.github.pokatomnik.kriper.screens.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

enum class DisplayType {
    INITIAL,
    ZOOM_AND_PAN
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageContent(url: String) {
    val state = remember { mutableStateOf(DisplayType.INITIAL) }

    if (state.value == DisplayType.INITIAL) {
        AsyncImage(
            model = url,
            contentDescription = url,
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {},
                    onDoubleClick = {
                        state.value = DisplayType.ZOOM_AND_PAN
                    }
                ),
            contentScale = ContentScale.Fit
        )
    }
    if (state.value == DisplayType.ZOOM_AND_PAN) {
        ScalableAsyncImage(
            url = url,
            onZoomBackToNormal = {
                state.value = DisplayType.INITIAL
            }
        )
    }
}