package com.github.pokatomnik.kriper.screens.story

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private fun Int.percentOf(max: Int): Int {
    return if (max == 0) 0 else 100 * this / max
}

@Composable
fun ScrollPositionIndication(scrollState: ScrollState) {
    val widthAnimated = animateFloatAsState(
        targetValue = scrollState
            .value
            .percentOf(scrollState.maxValue)
            .toFloat() / 100,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        progress = widthAnimated.value
    )
}