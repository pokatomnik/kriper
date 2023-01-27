package com.github.pokatomnik.kriper.screens.favoritestories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.widgets.LocalScaffoldState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun FavoriteStories(
    onNavigateBack: () -> Unit,
    onNavigateToStory: (storyTitle: String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = LocalScaffoldState.current
    rememberPreferences().globalPreferences.oneTimeRunners.oncePerInstallActions
        .Once("SWIPE_HELP") {
            scaffoldState?.let {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Свайп влево - добавить/убрать из избранного",
                        actionLabel = "Хорошо",
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }
        }

    val contentKeyState = remember { mutableStateOf(0) }

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
            PageTitle(title = "Избранное")
        },
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(false),
            onRefresh = {
                contentKeyState.value++
            },
            modifier = Modifier.fillMaxSize(),
            swipeEnabled = true,
        ) {
            FavoriteStoriesContent(
                swipeRefreshKey = contentKeyState.value,
                onNavigateToStory = onNavigateToStory
            )
        }
    }
}