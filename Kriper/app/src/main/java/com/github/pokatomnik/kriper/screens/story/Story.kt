package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.preferences.page.FontSize
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.components.BottomSheet
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun Story(
    storyTitle: String
) {
    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val bottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val (fontSize, setFontSize) = rememberPreferences().pagePreferences.fontSize.collectAsState()

    BottomSheet(
        drawerState = bottomDrawerState,
        content = {
            PageContainer {
                StoryScrollPosition(pageTitle = storyTitle) { scrollState ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = LARGE_PADDING.dp)
                            .verticalScroll(scrollState)
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
                            StoryContent(pageTitle = storyTitle, fontSize = fontSize)
                        }
                    }
                }
            }
        },
        drawerContent = {
            BottomDrawerContent(
                onResetFontSizePress = { setFontSize(FontSize.defaultFontSize) },
                onDecreaseFontSizePress = { setFontSize(fontSize - 1) },
                onIncreaseFontSizePress = { setFontSize(fontSize + 1) }
            )
        }
    )
}