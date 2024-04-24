package com.github.pokatomnik.kriper.screens.listbookmarks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.screens.addbookmark.rememberBookmarks
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.ConfirmationDialog
import com.github.pokatomnik.kriper.ui.components.ConfirmationDialogButton
import com.github.pokatomnik.kriper.ui.components.HorizontalSwipeableRow
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.LazyList
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.components.SwipeableActionParams
import com.github.pokatomnik.kriper.ui.components.embraceWithQuotationMarks
import com.github.pokatomnik.kriper.ui.widgets.BookmarkNavigationListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ListBookmarks(
    storyId: String?,
    onNavigateBack: () -> Unit,
    onNavigateToStoryWithScrollPosition: (
        storyId: String,
        scrollPosition: Int,
    ) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val bookmarksDAO = rememberKriperDatabase().bookmarksDAO()
    val (bookmarks, reloadBookmarks) = rememberBookmarks(storyId)
    val (confirmationMessage, setConfirmationMessage) = remember {
        mutableStateOf<String?>(null)
    }

    val handleRemoveBookmarkById: suspend (bookmarkId: Int) -> Unit = { bookmarkId ->
        try {
            bookmarksDAO.removeBookmarkByBookmarkId(bookmarkId)
            reloadBookmarks()
        } catch (e: Exception) { /* noop */ }
    }

    val removeAllBookmarks: (whenDone: () -> Unit) -> Unit = { whenDone ->
        coroutineScope.launch {
            withContext(Dispatchers.IO + SupervisorJob()) {
                try {
                    if (storyId == null) {
                        bookmarksDAO.removeAllBookmarks()
                    } else {
                        bookmarksDAO.removeAllBookmarksForStory(storyId)
                    }
                    reloadBookmarks()
                } finally { whenDone() }
            }
        }
    }

    DisplayHelpSideEffect()

    IndexServiceReadiness { indexService ->
        val pageMeta = storyId?.let { indexService.content.getPageMetaByStoryId(storyId) }

        val handleRemoveAllBookmarks: () -> Unit = {
            val message = storyId?.let {
                indexService.content.getPageMetaByStoryId(it)
            }.getRemoveConfirmationMessage()
            setConfirmationMessage(message)
        }

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
                PageTitle(
                    title = if (pageMeta == null) {
                        "Все закладки"
                    } else {
                        "Закладки для ${pageMeta.title.embraceWithQuotationMarks()}"
                    }
                )
            },
            trailingButton = {
                IconButton(onClick = handleRemoveAllBookmarks) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Удалить все"
                    )
                }
            }
        ) {
            if (bookmarks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = LARGE_PADDING.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "У вас нет закладок",
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = SMALL_PADDING.dp)) {
                    LazyList(list = bookmarks) { index, bookmark ->
                        val isFirst = 0 == index

                        if (isFirst) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(SMALL_PADDING.dp)
                            )
                        }
                        HorizontalSwipeableRow(
                            leftSwipeableHandler = object : SwipeableActionParams {
                                override val icon = Icons.Filled.Delete
                                override val contentDescription: String
                                    get() {
                                        val currentPageMeta = indexService.content.getPageMetaByStoryId(bookmark.storyId)
                                        val baseName = "Удалить закладку \"${bookmark.name}\""
                                        return if (currentPageMeta == null) baseName else "$baseName для истории \"${currentPageMeta.title}\""
                                    }
                                override suspend fun onSwipe(): suspend () -> Unit {
                                    return { handleRemoveBookmarkById(bookmark.id) }
                                }
                            }
                        ) {
                            BookmarkNavigationListItem(
                                bookmark = bookmark,
                                onPress = {
                                    onNavigateToStoryWithScrollPosition(
                                        bookmark.storyId,
                                        bookmark.scrollPosition
                                    )
                                }
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(LARGE_PADDING.dp)
                        )
                    }
                }
            }
        }

        if (confirmationMessage != null) {
            ConfirmationDialog(
                title = "Удалить?",
                description = confirmationMessage,
                confirm = object : ConfirmationDialogButton {
                    override val title = "Да"
                    override fun onPress() {
                        removeAllBookmarks { setConfirmationMessage(null) }
                    }
                },
                cancel = object : ConfirmationDialogButton {
                    override val title = "Нет"
                    override fun onPress() {
                        setConfirmationMessage(null)
                    }
                }
            )
        }
    }
}

private fun PageMeta?.getRemoveConfirmationMessage(): String {
    return when (this) {
        null -> "Точно удалить все закладки для всех историй?"
        else -> "Точно удалить все закладки для истории \"${title}\""
    }
}
//