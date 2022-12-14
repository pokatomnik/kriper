package com.github.pokatomnik.kriper.screens.taggroups

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.*
import com.github.pokatomnik.kriper.ui.components.PageTitle

@Composable
fun TagGroups(onNavigateToGroup: (tagName: String) -> Unit) {
    IndexServiceReadiness { indexService ->
        val groupNames = indexService.content.groupNames.toList()

        PageContainer(
            header = {
                PageTitle(title = "Группы тегов")
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = SMALL_PADDING.dp)
            ) {
                LazyList(list = groupNames) { index, groupTitle ->
                    val tagsString = indexService.content
                        .getTagGroupByName(groupTitle)
                        .tagNames
                        .joinToString(" ") { "#$it" }

                    val isFirst = 0 == index

                    if (isFirst) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(SMALL_PADDING.dp)
                        )
                    }
                    LargeCardNavigationListItem(
                        title = groupTitle,
                        description = {
                            Text(
                                text = tagsString,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.alpha(0.7f)
                            )
                        },
                        onClick = { onNavigateToGroup(groupTitle) }
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