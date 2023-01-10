package com.github.pokatomnik.kriper.screens.story

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.github.pokatomnik.kriper.ui.components.MarkdownText

@Composable
fun StoryContent(
    pageTitle: String,
    fontSize: Int,
) {
    StoryMarkdown(pageTitle = pageTitle) { storyMarkdown ->
        if (storyMarkdown != "") {
            MarkdownText(
                markdown = storyMarkdown,
                textAlign = TextAlign.Justify,
                disableLinkMovementMethod = true,
                fontSize = fontSize.sp,
            )
        }
    }
}