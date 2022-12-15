package com.github.pokatomnik.kriper.screens.taggroups

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.*

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
                        .shortIntro

                    val isFirst = 0 == index

                    if (isFirst) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(SMALL_PADDING.dp)
                        )
                    }
                    CardNavigationListItem(
                        title = groupTitle,
                        description = tagsString,
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