package com.github.pokatomnik.kriper.screens.story

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.contentColorFor
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
import com.github.pokatomnik.kriper.ui.components.LikeBox
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.widgets.LocalScaffoldState
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun Story(
    storyId: String,
    onNavigateToTag: (tag: String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToRandom: () -> Boolean,
    onNavigateToPrevious: () -> Boolean,
    onNavigateToVideo: (videoURL: String) -> Unit,
    onNavigateToAuthor: (authorRealName: String) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val scaffoldState = LocalScaffoldState.current
    val bottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val preferences = rememberPreferences()
    val globalPreferences = preferences.globalPreferences
    val pagePreferences = preferences.pagePreferences
    val (fontSize, setFontSize) = pagePreferences.fontSize.collectAsState()
    val fontInfoState = pagePreferences.storyContentFontFamily.collectAsState()
    val colorPresetState = pagePreferences.storyContentColorPreset.collectAsState()
    val favoriteState = rememberStoryFavorite(selectedStoryId = storyId)

    globalPreferences.oneTimeRunners.oncePerInstallActions.Once(key = "MENU_LIKE_INFO") {
        scaffoldState?.let {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Долгий тап для меню, двойной тап — в избранное",
                    actionLabel = "Понятно",
                    duration = SnackbarDuration.Indefinite
                )
            }
        }
    }
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
                        StoryScrollPosition(storyId) { scrollState ->
                            ScrollPositionIndication(
                                scrollState = scrollState,
                                colorsInfo = colorPresetState.value
                            )
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
                                        },
                                        onDoubleClick = {
                                            val oldLiked = favoriteState.state.value
                                            val newLiked = oldLiked == null || !oldLiked
                                            favoriteState.onFavoritePress(newLiked)
                                        }
                                    )
                            ) {
                                Column(modifier = Modifier.padding(vertical = LARGE_PADDING.dp)) {
                                    StoryTitle(storyId = storyId) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(LARGE_PADDING.dp)
                                        )
                                    }
                                    StoryDetails(
                                        storyId = storyId,
                                        colorsInfo = colorPresetState.value,
                                        onNavigateToAuthor = onNavigateToAuthor,
                                    ) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(SMALL_PADDING.dp)
                                        )
                                    }
                                    StoryTags(
                                        storyId = storyId,
                                        colorsInfo = colorPresetState.value,
                                        onTagClick = onNavigateToTag
                                    ) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(LARGE_PADDING.dp)
                                        )
                                    }
                                    StoryContent(
                                        storyId = storyId,
                                        fontSize = fontSize,
                                        fontInfo = fontInfoState.value,
                                        colorsInfo = colorPresetState.value
                                    ) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(LARGE_PADDING.dp)
                                        )
                                    }
                                    GalleryButton(
                                        storyId = storyId,
                                        colorsInfo = colorPresetState.value,
                                        onNavigateToGallery = onNavigateToGallery,
                                    ) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(LARGE_PADDING.dp)
                                        )
                                    }
                                    VideoButtons(
                                        storyId = storyId,
                                        colorsInfo = colorPresetState.value,
                                        onNavigateToVideo = onNavigateToVideo
                                    ) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(LARGE_PADDING.dp)
                                        )
                                    }
                                    SeeAlso(
                                        storyId = storyId,
                                        colorsInfo = colorPresetState.value,
                                        onNavigateToSearch = onNavigateToSearch,
                                    ) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(LARGE_PADDING.dp)
                                        )
                                    }
                                    SourceButton(
                                        storyId = storyId,
                                        colorsInfo = colorPresetState.value,
                                    ) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(LARGE_PADDING.dp)
                                        )
                                    }
                                    ReadButton(
                                        storyId = storyId,
                                        colorsInfo = colorPresetState.value,
                                    ) {}
                                }
                            }
                        }
                    }
                }
            }
            favoriteState.state.value?.let {
                LikeBox(
                    liked = it,
                    color = colorPresetState.value.contentColor ?: contentColorFor(
                        MaterialTheme.colors.surface
                    )
                )
            }
        },
        drawerContent = {
            ShareStoryControls(storyId = storyId)
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