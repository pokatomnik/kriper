package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness

@Composable
fun StoryTitle(
    storyId: String,
    displayAfter: @Composable () -> Unit
) {
    IndexServiceReadiness { indexService ->
        val pageMetaTitle = indexService.content.getPageMetaByStoryId(storyId)?.title ?: ""
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center,
                text = pageMetaTitle
            )
        }
        displayAfter()
    }
}