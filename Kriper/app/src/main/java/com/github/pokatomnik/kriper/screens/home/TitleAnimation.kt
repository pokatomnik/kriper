package com.github.pokatomnik.kriper.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch

@Composable
fun rememberTitleAnimation(): Pair<
    Animatable<Float, AnimationVector1D>,
    Animatable<Float, AnimationVector1D>
> {
    val animatedTitleTranslateX = remember { Animatable(-100f) }
    val animatedTitleAlpha = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        launch {
            animatedTitleTranslateX.animateTo(
                targetValue = 0f,
                animationSpec = tween(2000)
            )
        }
        launch {
            animatedTitleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(2000)
            )
        }
    }

    return Pair(animatedTitleTranslateX, animatedTitleAlpha)
}