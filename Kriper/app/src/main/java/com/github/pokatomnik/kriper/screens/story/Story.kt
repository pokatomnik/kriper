package com.github.pokatomnik.kriper.screens.story

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.preferences.page.FontSize
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.components.BottomSheet
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.widgets.ShowToastOncePerRunSideEffect
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun Story(
    storyTitle: String,
    onNavigateToTag: (tag: String) -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit,
    onNavigateToRandom: () -> Boolean,
    onNavigateToPrevious: () -> Boolean,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val bottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val pagePreferences = rememberPreferences().pagePreferences
    val (fontSize, setFontSize) = pagePreferences.fontSize.collectAsState()
    val fontInfoState = pagePreferences.storyContentFontFamily.collectAsState()
    val colorPresetState = pagePreferences.storyContentColorPreset.collectAsState()

    ShowToastOncePerRunSideEffect(message = "Долгое нажатие на текст для вызова меню")
    BottomSheet(
        drawerState = bottomDrawerState,
        content = {
            PageContainer {
                val offsetX = remember { Animatable(0f) }
                val maxDrag = 200f

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                        .draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState {
                                coroutineScope.launch {
                                    offsetX.snapTo(
                                        targetValue = (offsetX.value + it / 2)
                                            .coerceIn(-maxDrag..maxDrag)
                                    )
                                }
                            },
                            onDragStopped = {
                                val offsetXValue = -offsetX.value
                                val absoluteOffset = abs(offsetXValue)
                                val smallDrag = absoluteOffset < maxDrag - (maxDrag / 4)
                                if (smallDrag) {
                                    offsetX.animateTo(0f)
                                    return@draggable
                                }
                                val navigate =
                                    if (offsetXValue < 0) onNavigateToPrevious else onNavigateToRandom
                                val navigated = navigate()
                                if (!navigated) {
                                    offsetX.animateTo(0f)
                                }
                            }
                        )
                ) {
                    ContentSurface(colorPresetState = colorPresetState) {
                        StoryScrollPosition(pageTitle = storyTitle) { scrollState ->
                            ScrollPositionIndication(scrollState = scrollState)
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState)
                                    .padding(horizontal = LARGE_PADDING.dp)
                                    .combinedClickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = {},
                                        onLongClick = {
                                            coroutineScope.launch { bottomDrawerState.open() }
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                    )
                            ) {
                                Column(modifier = Modifier.padding(vertical = LARGE_PADDING.dp)) {
                                    StoryTitle(title = storyTitle)
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(LARGE_PADDING.dp)
                                    )
                                    StoryTags(
                                        pageTitle = storyTitle,
                                        colorsInfo = colorPresetState.value,
                                        onTagClick = onNavigateToTag
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(LARGE_PADDING.dp)
                                    )
                                    StoryContent(
                                        pageTitle = storyTitle,
                                        fontSize = fontSize,
                                        fontInfo = fontInfoState.value,
                                        colorsInfo = colorPresetState.value
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(LARGE_PADDING.dp)
                                    )
                                    SeeAlso(
                                        pageTitle = storyTitle,
                                        colorsInfo = colorPresetState.value,
                                        onStoryClick = onNavigateToStory
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        drawerContent = {
            FontSizeSelection(
                onResetFontSizePress = { setFontSize(FontSize.defaultFontSize) },
                onDecreaseFontSizePress = { setFontSize(fontSize - 1) },
                onIncreaseFontSizePress = { setFontSize(fontSize + 1) }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SMALL_PADDING.dp)
            )
            FontFamilySelection(fontInfoState = fontInfoState)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SMALL_PADDING.dp)
            )
            ContentColorSelection(colorPresetState = colorPresetState)
        }
    )
}