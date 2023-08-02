package com.github.pokatomnik.kriper.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING

@Composable
fun HeaderBlock() {
    val (animatedTitleTranslateX, animatedTitleAlpha) = rememberTitleAnimation()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(LARGE_PADDING.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "...Kriper",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.graphicsLayer {
                translationX = animatedTitleTranslateX.value
                alpha = animatedTitleAlpha.value
            }
        )
    }
}