package com.github.pokatomnik.kriper.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LikeBox(
    liked: Boolean,
    color: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    val (animationPlaying, setAnimationPlaying) = remember { mutableStateOf(false) }

    val sizeState = animateDpAsState(
        animationSpec = tween(durationMillis = 1000),
        targetValue = (if (liked) 100 else 0).dp,
        finishedListener = { setAnimationPlaying(false) }
    )

    DisposableEffect(liked) {
        onDispose {
            setAnimationPlaying(true)
        }
    }

    if (!animationPlaying) return

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(
            tint = color,
            modifier = Modifier
                .size(sizeState.value)
                .alpha(sizeState.value.value / 150)
                .blur(2.dp),
            imageVector = Icons.Filled.Favorite,
            contentDescription = if (liked) "В избранном" else "Не в избранном"
        )
    }
}