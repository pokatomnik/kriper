package com.github.pokatomnik.kriper.screens.story

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.pokatomnik.kriper.services.preferences.page.ColorsInfo

private fun Int.percentOf(max: Int): Int {
    return if (max == 0) 0 else 100 * this / max
}

@Composable
fun ScrollPositionIndication(
    scrollState: ScrollState,
    colorsInfo: ColorsInfo,
) {
    val widthAnimated = animateFloatAsState(
        targetValue = scrollState
            .value
            .percentOf(scrollState.maxValue)
            .toFloat() / 100,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        progress = widthAnimated.value,
        color = colorsInfo.contentColor ?: contentColorFor(
            MaterialTheme.colors.surface
        )
    )
}