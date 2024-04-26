package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.services.preferences.page.ColorsInfo
import com.github.pokatomnik.kriper.ui.components.ALPHA_GHOST
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.widgets.ShortDescriptionDialog

@Composable
fun ShortDescription(
    storyId: String,
    colorsInfo: ColorsInfo,
    displayAfter: @Composable () -> Unit,
) {
    IndexServiceReadiness { indexService ->
        val shortDescription = remember { mutableStateOf("") }

        LaunchedEffect(storyId) {
            shortDescription.value = indexService.content.getStoryShortDescriptionById(storyId)
        }

        if (shortDescription.value.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ShortDescriptionDialog(storyId = storyId) { showShortDescription ->
                        TextButton(onClick = showShortDescription) {
                            Text(
                                text = "АННОТАЦИЯ",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = colorsInfo.contentColor ?: contentColorFor(
                                    MaterialTheme.colors.surface
                                ),
                                modifier = Modifier.alpha(ALPHA_GHOST)
                            )
                        }
                    }
                }
            }
            displayAfter()
        }
    }
}