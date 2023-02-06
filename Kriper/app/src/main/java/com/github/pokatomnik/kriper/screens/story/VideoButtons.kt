package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.services.preferences.page.ColorsInfo
import com.github.pokatomnik.kriper.ui.components.ALPHA_GHOST
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun VideoButtons(
    pageTitle: String,
    colorsInfo: ColorsInfo,
    onNavigateToVideo: (videoURL: String) -> Unit,
    displayAfter: @Composable () -> Unit,
) {
    IndexServiceReadiness { indexService ->
        val videos = indexService.content.getPageMetaByName(pageTitle)?.videos
        if (!videos.isNullOrEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Прикрепленные видео:",
                        fontWeight = FontWeight.Bold,
                        color = colorsInfo.contentColor ?: contentColorFor(
                            MaterialTheme.colors.surface
                        ),
                        modifier = Modifier.alpha(ALPHA_GHOST)
                    )
                }
                FlowRow(modifier = Modifier.fillMaxWidth()) {
                    videos.forEachIndexed { index, currentVideoURL ->
                        TextButton(onClick = { onNavigateToVideo(currentVideoURL) }) {
                            Text(
                                text = "Видео ${index + 1}",
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