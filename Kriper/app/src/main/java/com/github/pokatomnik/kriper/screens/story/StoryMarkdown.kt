package com.github.pokatomnik.kriper.screens.story

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness

@Composable
fun StoryMarkdown(
    storyId: String,
    content: @Composable (markdown: String) -> Unit
) {
    IndexServiceReadiness { indexService ->
        val (storyMarkdown, setStoryMarkdown) = remember { mutableStateOf<String?>(null) }

        LaunchedEffect(storyId) {
            val markdown = indexService.content.getStoryMarkDownByStoryId(storyId).ifEmpty { null }
            setStoryMarkdown(markdown)
        }

        storyMarkdown?.let { content(storyMarkdown) }
    }
}