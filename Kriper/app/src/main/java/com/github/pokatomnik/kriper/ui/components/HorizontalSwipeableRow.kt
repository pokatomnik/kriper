package com.github.pokatomnik.kriper.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private const val SIDE_DRAG = 100
private const val ICON_GROWTH_MULTIPLIER = 1.5f

interface SwipeableActionParams {
    val icon: ImageVector
    val contentDescription: String
    suspend fun onSwipe()
}

@Composable
fun HorizontalSwipeableRow(
    leftSwipeableHandler: SwipeableActionParams? = null,
    rightSwipeableHandler: SwipeableActionParams? = null,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val offsetXInPixels = remember { Animatable(0f) }
    val minDragDp = if (leftSwipeableHandler == null) 0 else -SIDE_DRAG
    val maxDragDp = if (rightSwipeableHandler == null) 0 else SIDE_DRAG

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(SIDE_DRAG.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    rightSwipeableHandler?.let {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.contentDescription,
                            modifier = Modifier.graphicsLayer {
                                val scaleMultiplier =
                                    1 + abs(offsetXInPixels.value.asPixelsToDp(context)) / SIDE_DRAG * ICON_GROWTH_MULTIPLIER
                                scaleX = scaleMultiplier
                                scaleY = scaleMultiplier
                            }
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(SIDE_DRAG.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    leftSwipeableHandler?.let {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.contentDescription,
                            modifier = Modifier.graphicsLayer {
                                val scaleMultiplier =
                                    1 + abs(offsetXInPixels.value.asPixelsToDp(context)) / SIDE_DRAG * ICON_GROWTH_MULTIPLIER
                                scaleX = scaleMultiplier
                                scaleY = scaleMultiplier
                            }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset {
                        IntOffset(offsetXInPixels.value.roundToInt(), 0)
                    }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { deltaInPx ->
                            coroutineScope.launch {
                                offsetXInPixels.snapTo(
                                    targetValue = (offsetXInPixels.value + deltaInPx)
                                        .coerceIn(
                                            minDragDp.asDpToPixels(context)..maxDragDp.asDpToPixels(
                                                context
                                            )
                                        )
                                )
                            }
                        },
                        onDragStopped = {
                            val offsetXValueInPixels = -1 * offsetXInPixels.value
                            val absoluteOffset = abs(offsetXValueInPixels)
                            val smallDrag =
                                absoluteOffset < maxDragDp.asDpToPixels(context) - (maxDragDp.asDpToPixels(
                                    context
                                ) / 2)
                            if (smallDrag) {
                                offsetXInPixels.animateTo(0f)
                                return@draggable
                            }
                            val handler: SwipeableActionParams? = if (offsetXValueInPixels < 0) {
                                rightSwipeableHandler
                            } else if (offsetXValueInPixels > 0) {
                                leftSwipeableHandler
                            } else {
                                null
                            }
                            coroutineScope.launch { handler?.onSwipe() }
                            offsetXInPixels.animateTo(0f)
                        }
                    )
            ) { content() }
        }
    }
}