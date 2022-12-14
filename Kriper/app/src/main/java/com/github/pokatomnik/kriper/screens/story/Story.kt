package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun Story(
    storyTitle: String
) {
    val (storyMarkdown, setStoryMarkdown) = remember { mutableStateOf("") }

    IndexServiceReadiness { indexService ->
        LaunchedEffect(storyTitle) {
            val markdown = indexService.content.getStoryMarkdown(storyTitle)
            setStoryMarkdown(markdown)
        }

        PageContainer {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LARGE_PADDING.dp)
                    .verticalScroll(rememberScrollState())

            ) {
                Column(modifier = Modifier.padding(vertical = LARGE_PADDING.dp)) {
                    PageTitle(title = storyTitle)
                    Spacer(modifier = Modifier.fillMaxWidth().height(LARGE_PADDING.dp))
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
    }
}