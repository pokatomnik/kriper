package com.github.pokatomnik.kriper.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

interface ConfirmationDialogButton {
    val title: String
    fun onPress()
}

@Composable
fun ConfirmationDialog(
    title: String,
    description: String?,
    confirm: ConfirmationDialogButton,
    cancel: ConfirmationDialogButton,
    onClickOutside: () -> Unit = cancel::onPress
) {
    AlertDialog(
        modifier = Modifier.border(
            width = 2.dp,
            color = MaterialTheme.colors.primary,
            shape = MaterialTheme.shapes.small
        ),
        onDismissRequest = onClickOutside,
        title = {
            Text(text = title)
        },
        text = {
            description?.let { description ->
                Text(
                    text = description,
                    textAlign = TextAlign.Justify
                )
            }
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
                Button(onClick = confirm::onPress) {
                    Text(text = confirm.title)
                }
                Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                Button(onClick = cancel::onPress) {
                    Text(cancel.title)
                }
            }
        }
    )
}