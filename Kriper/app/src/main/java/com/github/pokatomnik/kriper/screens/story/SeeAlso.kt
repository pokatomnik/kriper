package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SeeAlso(
    pageTitle: String,
    onStoryClick: (storyTitle: String) -> Unit,
) {
    IndexServiceReadiness { indexService ->
        val seeAlsoTitles = indexService.content.getPageMetaByName(pageTitle)?.seeAlso
        if (!seeAlsoTitles.isNullOrEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "СМОТРИТЕ ТАКЖЕ:",
                        fontWeight = FontWeight.Bold
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
                                text = seeAlsoTitle
                            )
                        }
                    }
                }
            }
        }
    }
}
