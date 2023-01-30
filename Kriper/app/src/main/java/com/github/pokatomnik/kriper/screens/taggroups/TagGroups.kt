package com.github.pokatomnik.kriper.screens.taggroups

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Tag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ext.uppercaseFirst
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.*

@Composable
fun TagGroups(
    onNavigateToGroup: (tagGroup: String) -> Unit,
    onNavigateToStoriesOfTagOfTagGroup: (tagGroupName: String, tagName: String) -> Unit,
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = SMALL_PADDING.dp)
            ) {
                LazyList(list = groupNames) { index, groupTitle ->
                    val tagGroup = indexService.content
                        .getTagGroupByName(groupTitle)
                    val tagsInGroup = tagGroup.tagNames

                    val isFirst = 0 == index
                    if (isFirst) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(SMALL_PADDING.dp)
                        )
                    }
                    TextButton(onClick = { onNavigateToGroup(groupTitle) }) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = groupTitle
                        )
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "В группу меток"
                        )
                    }
                    LazyRow(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(tagsInGroup.toList()) { index, tagName ->
                            val isLast = index == tagsInGroup.size - 1
                            Spacer(
                                modifier = Modifier.width(SMALL_PADDING.dp)
                            )
                            IconicCardSmall(
                                icon = Icons.Filled.Tag,
                                title = tagName.uppercaseFirst(),
                                modifier = Modifier.width(128.dp),
                                onClick = {
                                    onNavigateToStoriesOfTagOfTagGroup(
                                        groupTitle,
                                        tagName
                                    )
                                }
                            )
                            if (isLast) {
                                Spacer(
                                    modifier = Modifier.width(SMALL_PADDING.dp)
                                )
                            }
                        }
                    }
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