package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.github.pokatomnik.kriper.ext.uppercaseFirst
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.ALPHA_GHOST
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment

@Composable
fun StoryTags(
    pageTitle: String,
    onTagClick: (tag: String) -> Unit
) {
    IndexServiceReadiness { indexService ->  
        indexService.content.getPageMetaByName(pageTitle)?.let { pageMeta ->  
            FlowRow(
                modifier = Modifier.fillMaxSize(),
                mainAxisAlignment = MainAxisAlignment.Center
            ) {
                pageMeta.tags.forEach { tag ->
                    TextButton(
                        onClick = {
                            onTagClick(tag)
                        }
                    ) {
                        Text(
                            text = "#${tag.uppercaseFirst()}",
                            modifier = Modifier.alpha(ALPHA_GHOST)
                        )
                    }
                }
            }
        }
    }
}