package com.github.pokatomnik.kriper.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.widgets.PageMetaLazyList

@Composable
fun History(
    onNavigateBack: () -> Unit,
    onNavigateToStoryById: (storyId: String) -> Unit,
) {
    PageContainer(
        priorButton = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад на главную"
                )
            }
        },
        header = {
            PageTitle(title = "Хронология")
        }
    ) {
        HistoryItems { pageMeta ->
            if (pageMeta.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(SMALL_PADDING.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Тут пока пусто")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = SMALL_PADDING.dp)
                ) {
                    PageMetaLazyList(
                        pageMeta = pageMeta,
                        onPageMetaClick = { onNavigateToStoryById(it.storyId) }
                    )
                }
            }
        }
    }
}