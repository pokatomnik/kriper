package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import com.github.pokatomnik.kriper.services.preferences.page.ColorsInfo
import com.github.pokatomnik.kriper.ui.components.ALPHA_GHOST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ReadButton(
    storyId: String,
    colorsInfo: ColorsInfo,
    displayAfter: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val kriperDatabase = rememberKriperDatabase()

    val (readState, updateReadState) = remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        launch {
            updateReadState(kriperDatabase.historyDAO().isRead(storyId))
        }
    }

    val setRead: (newRead: Boolean) -> Unit = { newRead ->
        updateReadState(newRead)
        scope.launch {
            withContext(Dispatchers.IO + SupervisorJob()) {
                try {
                    kriperDatabase.historyDAO().setRead(storyId, newRead)
                } catch (_: Exception) {
                    updateReadState(readState)
                }
            }
        }
    }

    val handleReadClick = {
        if (readState != null) {
            setRead(!readState)
        }
    }

    if (readState == null) {
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = handleReadClick) {
                Text(
                    text = if (readState) "ПОМЕТИТЬ КАК ПРОЧИТАННУЮ" else "ПОМЕТИТЬ КАК НЕПРОЧИТАННУЮ",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alpha(ALPHA_GHOST),
                    color = colorsInfo.contentColor ?: contentColorFor(
                        MaterialTheme.colors.surface
                    )
                )
            }
        }
    }
    displayAfter()
}