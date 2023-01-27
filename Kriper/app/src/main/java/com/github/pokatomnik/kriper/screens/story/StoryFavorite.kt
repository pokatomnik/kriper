package com.github.pokatomnik.kriper.screens.story

import androidx.compose.runtime.*
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import kotlinx.coroutines.launch


internal data class StoryFavoriteState(
    val state: MutableState<Boolean?>,
    val onFavoritePress: (isFavorite: Boolean) -> Unit
)

@Composable
internal fun rememberStoryFavorite(
    selectedPageTitle: String
): StoryFavoriteState {
    val coroutineScope = rememberCoroutineScope()
    val favoriteStoriesDAO = rememberKriperDatabase().favoriteStoriesDAO()
    val favoriteState = remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(selectedPageTitle) {
        val isCurrentFavorite = favoriteStoriesDAO.isFavorite(selectedPageTitle)
        favoriteState.value = isCurrentFavorite
    }
    val onFavoritePress: (isFavorite: Boolean) -> Unit = {
        favoriteState.value = it
        coroutineScope.launch {
            if (it) {
                favoriteStoriesDAO.addToFavorites(selectedPageTitle)
            } else {
                favoriteStoriesDAO.removeFromFavorites(selectedPageTitle)
            }
        }
    }

    return StoryFavoriteState(
        state = favoriteState,
        onFavoritePress = onFavoritePress
    )
}