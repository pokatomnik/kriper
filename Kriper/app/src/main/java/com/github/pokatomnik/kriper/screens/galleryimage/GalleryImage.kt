package com.github.pokatomnik.kriper.screens.galleryimage

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import coil.compose.AsyncImage
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import kotlin.math.roundToInt

/**
 * Rotation temporarily disabled here.
 * Get back if needed
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryImage(
    storyTitle: String,
    imageIndex: Int,
    onNavigateBack: () -> Unit,
) {
    val displayHeaderState = remember { mutableStateOf(false) }
    val toggleHeaderState = { displayHeaderState.value = !displayHeaderState.value }

    IndexServiceReadiness { indexService ->
        indexService.content.getPageMetaByName(storyTitle)?.let { pageMeta ->
            val imageUrl = pageMeta.images.toList()[imageIndex]
            PageContainer(
                priorButton = if (displayHeaderState.value) ({
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }) else null,
                header = if (displayHeaderState.value) ({
                    PageTitle(title = "Изображение")
                }) else null
            ) {
//                val angleState = remember { mutableStateOf(0f) }
                val zoomState = remember { mutableStateOf(1f) }
                val offsetXState = remember { mutableStateOf(0f) }
                val offsetYState = remember { mutableStateOf(0f) }

//                val angleStateAnimated = animateFloatAsState(targetValue = angleState.value)
                val zoomStateAnimated = animateFloatAsState(targetValue = zoomState.value)
                val offsetXStateAnimated = animateFloatAsState(targetValue = offsetXState.value)
                val offsetYStateAnimated = animateFloatAsState(targetValue = offsetYState.value)

                val resetView = {
//                    angleState.value = 0f
                    zoomState.value = 1f
                    offsetXState.value = 0f
                    offsetYState.value = 0f
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                        .combinedClickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = toggleHeaderState,
                            onDoubleClick = resetView
                        )
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    offsetXStateAnimated.value.roundToInt(),
                                    offsetYStateAnimated.value.roundToInt()
                                )
                            }
                            .graphicsLayer(
                                scaleX = zoomStateAnimated.value,
                                scaleY = zoomStateAnimated.value,
//                                rotationZ = angleStateAnimated.value
                            )
                            .pointerInput(Unit) {
                                detectTransformGestures(
                                    onGesture = {
                                            _,
                                            pan,
                                            gestureZoom,
//                                            gestureRotate,
                                            _
                                            ->
//                                        angleState.value += gestureRotate
                                        zoomState.value = (zoomState.value * gestureZoom).coerceIn(1f, 5f)
                                        val x = pan.x * zoomState.value
                                        val y = pan.y * zoomState.value
//                                        val angleRad = angleState.value * PI / 180.0
//                                        offsetXState.value += (x * cos(angleRad) - y * sin(angleRad)).toFloat()
//                                        offsetYState.value += (x * sin(angleRad) + y * cos(angleRad)).toFloat()
                                        offsetXState.value += x
                                        offsetYState.value += y
                                    }
                                )
                            }
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}