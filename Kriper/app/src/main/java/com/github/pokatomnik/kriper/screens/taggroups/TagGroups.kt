package com.github.pokatomnik.kriper.screens.taggroups

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ext.getPluralNoun
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.*

@Composable
fun TagGroups(
    onNavigateToGroup: (tagName: String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    IndexServiceReadiness { indexService ->
        val groupNames = indexService.content.groupNames.toList()

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
                PageTitle(title = "Группы тегов")
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = SMALL_PADDING.dp)
            ) {
                LazyList(list = groupNames) { index, groupTitle ->
                    val tagGroup = indexService.content
                        .getTagGroupByName(groupTitle)

                    val tagsString = tagGroup.shortIntro
                    val tagsInGroup = tagGroup.tagNames.size
                    val tagsInGroupPlural = tagsInGroup.getPluralNoun(
                        "метка",
                        "метки",
                        "меток"
                    )

                    val isFirst = 0 == index

                    if (isFirst) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(SMALL_PADDING.dp)
                        )
                    }
                    CardNavigationListItem(
                        title = "$groupTitle, $tagsInGroup $tagsInGroupPlural",
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