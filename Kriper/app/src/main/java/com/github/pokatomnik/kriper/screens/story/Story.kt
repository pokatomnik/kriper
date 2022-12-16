package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.BottomSheet
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import dev.jeziellago.compose.markdowntext.MarkdownText
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
    val (storyMarkdown, setStoryMarkdown) = remember { mutableStateOf("") }

    IndexServiceReadiness { indexService ->
        LaunchedEffect(storyTitle) {
            val markdown = indexService.content.getStoryMarkdown(storyTitle)
            setStoryMarkdown(markdown)
        }

        BottomSheet(
            drawerState = bottomDrawerState,
            content = {
                PageContainer {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = LARGE_PADDING.dp)
                            .verticalScroll(rememberScrollState())
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
                            PageTitle(title = storyTitle)
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(LARGE_PADDING.dp)
                            )
                            if (storyMarkdown != "") {
                                MarkdownText(
                                    markdown = storyMarkdown,
                                    textAlign = TextAlign.Justify,
                                    disableLinkMovementMethod = true,
                                )
                            }
                        }
                    }
                }
            },
            drawerContent = {
                BottomDrawerContent(
                    onResetFontSizePress = {},
                    onDecreaseFontSizePress = {},
                    onIncreaseFontSizePress = {}
                )
            }
        )
    }
}