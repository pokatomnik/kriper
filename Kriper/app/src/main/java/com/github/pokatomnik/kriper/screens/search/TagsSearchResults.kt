package com.github.pokatomnik.kriper.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ext.getPluralNoun
import com.github.pokatomnik.kriper.ext.uppercaseFirst
import com.github.pokatomnik.kriper.services.index.TagContents
import com.github.pokatomnik.kriper.ui.components.CardNavigationListItem
import com.github.pokatomnik.kriper.ui.components.LazyList
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING

@Composable
fun TagsSearchResults(
    isSearching: Boolean,
    tagContentItems: Collection<TagContents>?,
    onNavigateToTag: (tagTitle: String) -> Unit
) {
    if (isSearching) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SMALL_PADDING.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else if (tagContentItems == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SMALL_PADDING.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Тут будут найденные метки")
        }
    } else if (tagContentItems.isEmpty()) {
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
                .padding(horizontal = SMALL_PADDING.dp)
        ) {
            LazyList(list = tagContentItems.toList()) { index, tagContents ->
                val isFirst = 0 == index
                val storiesInTag = tagContents.storyIds.size
                val shortIntro = tagContents.shortIntro

                val storiesPlural = storiesInTag.getPluralNoun(
                    form1 = "история",
                    form2 = "истории",
                    form3 = "историй"
                )

                if (isFirst) {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(SMALL_PADDING.dp))
                }
                CardNavigationListItem(
                    title = "#${tagContents.tagName.uppercaseFirst()}, $storiesInTag $storiesPlural",
                    description = shortIntro,
                    iconPainter = tagContents.image(),
                    onClick = { onNavigateToTag(tagContents.tagName) }
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