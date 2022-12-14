package com.github.pokatomnik.kriper.screens.storiesoftag

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LazyList
import com.github.pokatomnik.kriper.ui.components.OneRowNavigationListItem
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle

@Composable
fun StoriesOfTag(
    tagGroupName: String,
    tagName: String,
    onNavigateBack: () -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit
) {
    IndexServiceReadiness { indexService ->
        PageContainer(
            priorButton = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад к тегу $tagName"
                    )
                }
            },
            header = {
                PageTitle(title = tagName)
            }
        ) {
            LazyList(
                list = indexService.content
                    .getTagGroupByName(tagGroupName)
                    .getTagContentsByName(tagName)
                    .pageNames
                    .toList()
            ) { pageTitle ->
                OneRowNavigationListItem(
                    title = pageTitle,
                    onClick = { onNavigateToStory(pageTitle) }
                )
            }
        }
    }
}