package com.github.pokatomnik.kriper.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ext.getPluralNoun
import com.github.pokatomnik.kriper.services.index.TagGroup
import com.github.pokatomnik.kriper.ui.components.CardNavigationListItem
import com.github.pokatomnik.kriper.ui.components.LazyList
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING

@Composable
fun TagGroupSearchResults(
    tagGroups: Collection<TagGroup>?,
    onNavigateToTagGroup: (tagGroupTitle: String) -> Unit,
) {
    if (tagGroups == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SMALL_PADDING.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Тут будут найденные группы меток")
        }
    } else if (tagGroups.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SMALL_PADDING.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Тут ничего не найдено")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SMALL_PADDING.dp),
        ) {
            LazyList(list = tagGroups.toList()) { index, tagGroup ->
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
                    title = "${tagGroup.tagGroupName}, $tagsInGroup $tagsInGroupPlural",
                    description = tagsString,
                    onClick = { onNavigateToTagGroup(tagGroup.tagGroupName) }
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