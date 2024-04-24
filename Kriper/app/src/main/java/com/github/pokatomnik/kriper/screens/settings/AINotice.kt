package com.github.pokatomnik.kriper.screens.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.KRIPER_DOMAIN
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING

@Composable
fun AINotice() {
    val dialogOpen = remember { mutableStateOf(false) }
    val showDialog = { dialogOpen.value = true }
    val hideDialog = { dialogOpen.value = false }

    TextButton(onClick = showDialog) {
        Text("AI в приложении")
    }
    if (dialogOpen.value) {
        AlertDialog(
            modifier = Modifier.border(
                width = 2.dp,
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.small
            ),
            onDismissRequest = hideDialog,
            title = { Text("AI POWERS") },
            text = {
                Text(
                    text = "При создании приложения использованы AI инструменты, " +
                            "в частности, короткие описания историй сгенерированы с помощью " +
                            "нейросети YandexGPT. Разработчики и авторы настоящего приложения " +
                            "не имеют никакого отношения к контенту, который сгенерирован " +
                            "этой нейросетью. В случае если материал, сгенерированный нейросетью " +
                            "кажется оскорбительным, вводящим в заблуждения или просто не нравится " +
                            "по любой причине, просим Вас воздержаться от использования данного " +
                            "приложения и просим прощения за предоставленные неудобства. " +
                            "Так же будем признательны за любую обратную связь касаемо " +
                            "настоящего приложения или отдельно этой функциональности.",
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
                    Button(onClick = hideDialog) {
                        Text("Понятно")
                    }
                }
            },
        )
    }
}