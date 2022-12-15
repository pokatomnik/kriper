package com.github.pokatomnik.kriper.screens.storiesoftag

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ext.uppercaseFirst
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.*
import com.github.pokatomnik.kriper.ui.components.PageTitle

@Composable
fun StoriesOfTag(
    tagGroupName: String,
    tagName: String,
    onNavigateBack: () -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit
) {
    IndexServiceReadiness { indexService ->
        val tagContents = indexService.content
            .getTagGroupByName(tagGroupName)
            .getTagContentsByName(tagName)
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
                PageTitle(title = "#${tagName.uppercaseFirst()}")
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = SMALL_PADDING.dp)
            ) {
                LazyList(list = tagContents.pageNames.toList()) { index, pageTitle ->
                    val isFirst = 0 == index
                    val pageMeta = indexService.content.getPageMetaByName(pageTitle)

                    if (isFirst) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(SMALL_PADDING.dp)
                        )
                    }
                    StoryCardNavigationListItem(
                        title = pageTitle,
                        tags = pageMeta?.tags ?: listOf(),
                        rating = pageMeta?.rating ?: 0,
                        author = pageMeta?.authorNickname ?: "Автор не указан",
                        readingTimeMinutes = pageMeta?.readingTimeMinutes ?: 0f,
                        onClick = { onNavigateToStory(pageTitle) }
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(SMALL_PADDING.dp)
                    )
                }
            }
        }
    }
}