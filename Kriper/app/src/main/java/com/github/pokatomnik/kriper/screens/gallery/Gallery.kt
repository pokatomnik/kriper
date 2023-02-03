package com.github.pokatomnik.kriper.screens.gallery

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Gallery(
    storyTitle: String,
    onNavigateBack: () -> Unit,
) {
    IndexServiceReadiness { indexService ->
        PageContainer(
            priorButton = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            },
            header = { PageTitle(title = storyTitle) }
        ) {
            indexService.content.getPageMetaByName(storyTitle)?.images?.let { imageURLs ->
                val imageUrlsList = imageURLs.toList()
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    count = imageURLs.size
                ) { index ->
                    val url = imageUrlsList[index]
                    ImageContent(url = url)
                }
            }
        }
    }
}