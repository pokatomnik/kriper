package com.github.pokatomnik.kriper.screens.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.github.pokatomnik.kriper.R
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Gallery(
    storyId: String,
    onNavigateBack: () -> Unit,
    onNavigateToImage: (index: Int) -> Unit
) {
    IndexServiceReadiness { indexService ->
        indexService.content.getPageMetaByStoryId(storyId)?.images?.let { imageURLs ->
            val state = rememberPagerState(0)
            PageContainer(
                priorButton = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                header = {
                    PageTitle(title = "${state.currentPage + 1} / ${state.pageCount}")
                }
            ) {
                val imageUrlsList = imageURLs.toList()
                HorizontalPager(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                    count = imageURLs.size
                ) { index ->
                    imageUrlsList[index].let { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = url,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = { onNavigateToImage(index) }
                                ),
                            contentScale = ContentScale.Fit,
                            placeholder = painterResource(id = R.drawable.loading_placeholder),
                            error = painterResource(id = R.drawable.error_placeholder)
                        )
                    }
                }
            }
        }
    }
}