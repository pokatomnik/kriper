package com.github.pokatomnik.kriper.screens.gallery

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

private const val MIN_SCALE = 0f
private const val MAX_SCALE = 3f

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScalableAsyncImage(
    url: String,
    modifier: Modifier = Modifier,
    onZoomBackToNormal: () -> Unit = {},
) {
    val zoomMS = remember { mutableStateOf(1f) }
    val offsetXMS = remember { mutableStateOf(0f) }
    val offsetYMS = remember { mutableStateOf(0f) }
    
    val zoom = animateFloatAsState(
        targetValue = zoomMS.value,
        finishedListener = {
            if (it == 1f) { onZoomBackToNormal() }
        }
    )
    val offsetX = animateFloatAsState(targetValue = offsetXMS.value)
    val offsetY = animateFloatAsState(targetValue = offsetYMS.value)

    LaunchedEffect(Unit) {
        zoomMS.value = MAX_SCALE
    }

    AsyncImage(
        model = url,
        contentDescription = "some description here",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .graphicsLayer(
                scaleX = zoom.value,
                scaleY = zoom.value,
                translationX = offsetX.value,
                translationY = offsetY.value,
            )
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { _, pan, gestureZoom, _ ->
                        zoomMS.value = (zoom.value * gestureZoom).coerceIn(MIN_SCALE, MAX_SCALE)
                        if (zoom.value <= 1f) {
                            onZoomBackToNormal()
                        }
                        offsetXMS.value += pan.x * zoom.value
                        offsetYMS.value += pan.y * zoom.value
                    }
                )
            }
            .fillMaxSize()
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {},
                onDoubleClick = {
                    zoomMS.value = 1f
                    offsetXMS.value = 0f
                    offsetYMS.value = 0f
                }
            )
    )
}