package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING

@Composable
fun AddBookmarkButtonRow(
    onAddBookmarkPress: () -> Unit,
    onListBookmarksPress: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().height(32.dp).padding(horizontal = LARGE_PADDING.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Закладки",
                    fontWeight = FontWeight.Bold
                )
            }
            Column {
                Row {
                    IconButton(onClick = onListBookmarksPress) {
                        Icon(
                            imageVector = Icons.Filled.Bookmarks,
                            contentDescription = "Открыть список закладок для этой истории"
                        )
                    }
                    IconButton(onClick = onAddBookmarkPress) {
                        Icon(
                            imageVector = Icons.Filled.BookmarkAdd,
                            contentDescription = "Добавить в закладки"
                        )
                    }
                }
            }
        }
    }
}