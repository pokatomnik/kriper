package com.github.pokatomnik.kriper.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import kotlinx.coroutines.launch

const val DEFAULT_EMPTY = "Короткое описание отсутствует"

private data class ShortDescriptionHolder(val shortDescription: String)

@Composable
fun ShortDescriptionDialog(
    storyId: String,
    consumer: @Composable (show: () -> Unit) -> Unit
) {
    val scope = rememberCoroutineScope()
    val visibility = remember { mutableStateOf(false) }
    val shortDescription = remember { mutableStateOf<ShortDescriptionHolder?>(null) }

    if (visibility.value) {
        Dialog(
            properties = DialogProperties(
                decorFitsSystemWindows = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
            onDismissRequest = { visibility.value = false },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.8f)
                    .background(MaterialTheme.colors.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .padding(LARGE_PADDING.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = shortDescription.value.let { shortDescriptionHolder ->
                                shortDescriptionHolder
                                    ?.shortDescription
                                    ?.ifEmpty { DEFAULT_EMPTY }
                                    ?: ""
                            },
                            textAlign = TextAlign.Justify
                        )
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { visibility.value = false }
                    ) {
                        Text("Отлично")
                    }
                }
            }
        }
    }

    IndexServiceReadiness { indexService ->
        val showShortDescription: () -> Unit = {
            if (shortDescription.value != null) {
                visibility.value = true
            } else scope.launch {
                val newHolder = ShortDescriptionHolder(
                    shortDescription = indexService.content.getStoryShortDescriptionById(storyId)
                )
                shortDescription.value = newHolder
                visibility.value = true
            }
        }

        consumer(showShortDescription)
    }
}