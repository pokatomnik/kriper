package com.github.pokatomnik.kriper.screens.favoritestories

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.db.dao.favoritestories.DisplayHelpSideEffect
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.components.makeToast
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FavoriteStories(
    onNavigateBack: () -> Unit,
    onNavigateToStoryById: (storyId: String) -> Unit
) {
    val toast = makeToast(Toast.LENGTH_LONG)
    val coroutineScope = rememberCoroutineScope()
    val favoriteStoriesDAO = rememberKriperDatabase().favoriteStoriesDAO()
    val contentKeyState = remember { mutableStateOf(0) }

    val removeConfirmationDisplayedState = remember { mutableStateOf(false) }
    val hideModal = { removeConfirmationDisplayedState.value = false }
    val showModalIfNeeded: () -> Unit = {
        coroutineScope.launch {
            val rowsTotal = withContext(Dispatchers.IO + SupervisorJob()) {
                favoriteStoriesDAO.getFavoriteQuantity()
            }
            if (rowsTotal == 0) {
                toast("Нечего удалять")
            } else {
                removeConfirmationDisplayedState.value = true
            }
        }
    }

    val removeAllAndCloseDialog: () -> Unit = {
        hideModal()
        coroutineScope.launch {
            withContext(Dispatchers.IO + SupervisorJob()) {
                favoriteStoriesDAO.clearAllFavoriteTitles()
            }
            ++contentKeyState.value
            toast("Избранное очищено")
        }
    }

    if (removeConfirmationDisplayedState.value) {
        AlertDialog(
            modifier = Modifier.border(
                width = 2.dp,
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.small
            ),
            onDismissRequest = hideModal,
            title = { Text("Удалить всё?") },
            text = {
                Text(
                    text = "Всё избранное будет удалено, но истории будут по-прежнему доступны через поиск или навигацию по разделам и меткам",
                    textAlign = TextAlign.Justify
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 0.dp,
                            start = LARGE_PADDING.dp,
                            end = LARGE_PADDING.dp,
                            bottom = LARGE_PADDING.dp
                        ),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = removeAllAndCloseDialog) {
                        Text("Удалить")
                    }
                    Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                    Button(onClick = hideModal) {
                        Text("Не надо")
                    }
                }
            }
        )
    }
    DisplayHelpSideEffect()
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
            PageTitle(title = "Избранное")
        },
        trailingButton = {
            IconButton(onClick = showModalIfNeeded) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Удалить всё"
                )
            }
        }
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
                onNavigateToStoryById = onNavigateToStoryById
            )
        }
    }
}