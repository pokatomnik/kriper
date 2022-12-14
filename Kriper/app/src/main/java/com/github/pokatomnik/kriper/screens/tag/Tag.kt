package com.github.pokatomnik.kriper.screens.tag

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ext.getPluralNoun
import com.github.pokatomnik.kriper.ext.uppercaseFirst
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.*
import com.github.pokatomnik.kriper.ui.components.PageTitle

@Composable
fun Tag(
    tagGroupTitle: String,
    navigateBack: () -> Unit,
    navigateToStories: (tagName: String) -> Unit
) {
    IndexServiceReadiness { indexService ->
        val tagGroup = indexService.content
            .getTagGroupByName(tagGroupTitle)
        val tags = tagGroup.tagNames.toList()

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
                PageTitle(title = "#$tagGroupTitle")
            }
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = SMALL_PADDING.dp)) {
                LazyList(list = tags) { index, tagTitle ->
                    val tagContents = tagGroup.getTagContentsByName(tagTitle)
                    val isFirst = 0 == index

                    val storiesInTag = tagContents.pageNames.size
                    val shortIntro = tagContents.shortIntro

                    if (isFirst) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(SMALL_PADDING.dp)
                        )
                    }
                    val storiesPlural = storiesInTag.getPluralNoun(
                        form1 = "история",
                        form2 = "истории",
                        form3 = "историй"
                    )
                    LargeCardNavigationListItem(
                        title = "#${tagTitle.uppercaseFirst()}, $storiesInTag $storiesPlural",
                        description = {
                            Text(
                                text = shortIntro,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.alpha(ALPHA_GHOST)
                            )
                        },
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