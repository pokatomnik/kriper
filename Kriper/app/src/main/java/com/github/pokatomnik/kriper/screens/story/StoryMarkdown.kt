package com.github.pokatomnik.kriper.screens.story

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness

@Composable
fun StoryMarkdown(
    pageTitle: String,
    content: @Composable (markdown: String) -> Unit
) {
    IndexServiceReadiness { indexService ->
        val (storyMarkdown, setStoryMarkdown) = remember { mutableStateOf<String?>(null) }

        LaunchedEffect(pageTitle) {
            val markdown = indexService.content.getStoryMarkdown(pageTitle).ifEmpty { null }
            setStoryMarkdown(markdown)
        }

        storyMarkdown?.let { content(storyMarkdown) }
    }
}