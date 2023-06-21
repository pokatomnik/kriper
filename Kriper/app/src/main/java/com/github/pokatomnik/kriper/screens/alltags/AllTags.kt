package com.github.pokatomnik.kriper.screens.alltags

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ext.getPluralNoun
import com.github.pokatomnik.kriper.ext.uppercaseFirst
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.*
import com.github.pokatomnik.kriper.ui.components.PageTitle

@Composable
fun AllTags(
    onNavigateBack: () -> Unit,
    navigateToStories: (tagName: String) -> Unit,
) {
    IndexServiceReadiness { indexService ->
        val allTagsGroup = indexService.content.allTagsGroup
        val allTags = allTagsGroup.tagNames

        PageContainer(
            priorButton = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            },
            header = {
                PageTitle(title = "Все метки")
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = SMALL_PADDING.dp)
            ) {
                LazyList(list = allTags.toList()) { index, tagTitle ->
                    val tagContents = allTagsGroup.getTagContentsByName(tagTitle)
                    val isFirst = 0 == index

                    val storiesInTag = tagContents.storyIds.size
                    val shortIntro = tagContents.shortIntro

                    val storiesPlural = storiesInTag.getPluralNoun(
                        form1 = "история",
                        form2 = "истории",
                        form3 = "историй"
                    )

                    if (isFirst) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(SMALL_PADDING.dp)
                        )
                    }
                    CardNavigationListItem(
                        title = "#${tagTitle.uppercaseFirst()}, $storiesInTag $storiesPlural",
                        description = shortIntro,
                        iconPainter = tagContents.image(),
                        onClick = { navigateToStories(tagTitle) }
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