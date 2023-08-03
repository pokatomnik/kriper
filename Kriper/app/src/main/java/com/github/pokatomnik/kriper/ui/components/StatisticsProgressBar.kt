package com.github.pokatomnik.kriper.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun StatisticsProgressBar(
    title: String,
    current: Int,
    max: Int,
    animationMillis: Int = 2000,
) {
    val currentProgress = current.toFloat() / max.toFloat()
    val progressAnimated = rememberProgressBarAnimation(currentProgress, animationMillis)
    val valueAnimated = animatePositiveInt(current.toLong(), animationMillis)
    Box(
        modifier = Modifier
            .width(128.dp)
            .height(128.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(123.dp)
                .height(123.dp),
            progress = 1f,
            color = MaterialTheme.colors.secondary,
            strokeWidth = 1.dp
        )
        CircularProgressIndicator(
            modifier = Modifier
                .width(128.dp)
                .height(128.dp),
            progress = progressAnimated.value,
            color = MaterialTheme.colors.secondary,
            strokeWidth = 6.dp
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)
            Text(text = "$valueAnimated / $max")
        }
    }
}

@Composable
fun animatePositiveInt(to: Long, durationMillis: Int): Long {
    val stepDuration = 50L
    val steps = durationMillis / stepDuration
    val currentValueState = remember { mutableStateOf(0L) }
    LaunchedEffect(to) {
        currentValueState.value = 0
        launch {
            for (step in 0 until steps) {
                currentValueState.value += max(to / steps, 1)
                delay(stepDuration)
            }
            currentValueState.value = to
        }
    }
    return currentValueState.value
}

@Composable
fun rememberProgressBarAnimation(to: Float, animationMillis: Int): Animatable<Float, AnimationVector1D> {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(to) {
        launch {
            animatedProgress.animateTo(
                targetValue = to,
                animationSpec = tween(animationMillis)
            )
        }
    }

    return animatedProgress
}