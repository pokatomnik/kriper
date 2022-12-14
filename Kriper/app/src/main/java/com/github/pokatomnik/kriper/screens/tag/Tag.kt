package com.github.pokatomnik.kriper.screens.tag

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
fun Tag(
    tagGroupTitle: String,
    navigateBack: () -> Unit,
    navigateToStories: (tagName: String) -> Unit
) {
    IndexServiceReadiness { indexService ->
        PageContainer(
            priorButton = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад к группам тегов"
                    )
                }
            },
            header = {
                PageTitle(title = tagGroupTitle)
            }
        ) {
            LazyList(
                list = indexService.content
                    .getTagGroupByName(tagGroupTitle)
                    .tagNames
                    .toList()
            ) { _, tagTitle ->
                OneRowNavigationListItem(
                    title = tagTitle,
                    onClick = { navigateToStories(tagTitle) }
                )
            }
        }
    }
}