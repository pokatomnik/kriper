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
fun SeeAlso(
    pageTitle: String,
    colorsInfo: ColorsInfo,
    onStoryClick: (storyTitle: String) -> Unit,
) {
    IndexServiceReadiness { indexService ->
        val seeAlsoTitles = indexService.content.getPageMetaByName(pageTitle)?.seeAlso
        if (!seeAlsoTitles.isNullOrEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "СМОТРИТЕ ТАКЖЕ:",
                        fontWeight = FontWeight.Bold,
                        color = colorsInfo.contentColor ?: contentColorFor(
                            MaterialTheme.colors.surface
                        ),
                        modifier = Modifier.alpha(ALPHA_GHOST),
                    )
                }
                FlowRow(modifier = Modifier.fillMaxWidth()) {
                    seeAlsoTitles.forEach { seeAlsoTitle ->
                        TextButton(
                            onClick = {
                                onStoryClick(seeAlsoTitle)
                            }
                        ) {
                            Text(
                                text = seeAlsoTitle,
                                color = colorsInfo.contentColor ?: contentColorFor(
                                    MaterialTheme.colors.surface
                                ),
                                modifier = Modifier.alpha(ALPHA_GHOST),
                            )
                        }
                    }
                }
            }
        }
    }
}
