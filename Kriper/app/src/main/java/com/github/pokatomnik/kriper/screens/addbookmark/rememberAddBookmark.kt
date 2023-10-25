package com.github.pokatomnik.kriper.screens.addbookmark

import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

@Composable
fun rememberAddBookmark(): suspend (
    name: String,
    storyId: String,
    scrollPosition: Int,
) -> Unit {
    val bookmarksDAO = rememberKriperDatabase().bookmarksDAO()
    val addBookmark: suspend (
        name: String,
        storyId: String,
        scrollPosition: Int,
    ) -> Unit = { name, storyId, scrollPosition ->
        withContext(Dispatchers.IO + SupervisorJob()) {
            bookmarksDAO.addBookmark(
                storyId = storyId,
                name = name,
                scrollPosition = scrollPosition
            )
        }
    }

    return addBookmark
}