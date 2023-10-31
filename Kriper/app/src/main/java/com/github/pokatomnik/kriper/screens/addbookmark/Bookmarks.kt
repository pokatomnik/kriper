package com.github.pokatomnik.kriper.screens.addbookmark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.github.pokatomnik.kriper.services.db.dao.bookmarks.Bookmark
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun rememberBookmarks(storyId: String?): Pair<List<Bookmark>, () -> Unit> {
    val bookmarksDAO = rememberKriperDatabase().bookmarksDAO()
    val scope = rememberCoroutineScope()

    val (bookmarks, setBookmarks) = remember {
        mutableStateOf(listOf<Bookmark>())
    }

    val loadBookmarks: () -> Unit = {
        scope.launch {
            withContext(Dispatchers.IO + SupervisorJob()) {
                try {
                    val loadedBookmarks = if (storyId == null) {
                        bookmarksDAO.getAll()
                    } else {
                        bookmarksDAO.getByStoryId(storyId)
                    }
                    setBookmarks(loadedBookmarks)
                } catch (e: Exception) {
                    setBookmarks(listOf())
                }
            }
        }
    }

    LaunchedEffect(storyId) { loadBookmarks() }

    return Pair(bookmarks, loadBookmarks)
}