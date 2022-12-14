package com.github.pokatomnik.kriper.screens.taggroups

import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LazyList
import com.github.pokatomnik.kriper.ui.components.OneRowNavigationListItem
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle

@Composable
fun TagGroups(onNavigateToGroup: (tagName: String) -> Unit) {
    IndexServiceReadiness { indexService ->
        PageContainer(
            header = {
                PageTitle(title = "Группы тегов")
            }
        ) {
            LazyList(list = indexService.content.groupNames.toList()) { title ->
                OneRowNavigationListItem(
                    title = title,
                    onClick = { onNavigateToGroup(title) }
                )
            }
        }
    }
}