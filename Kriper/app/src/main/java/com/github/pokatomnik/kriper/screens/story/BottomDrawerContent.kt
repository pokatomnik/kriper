package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.YoutubeSearchedFor
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING

@Composable
fun BottomDrawerContent(
    onResetFontSizePress: () -> Unit,
    onIncreaseFontSizePress: () -> Unit,
    onDecreaseFontSizePress: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = LARGE_PADDING.dp)) {
        Row(
            modifier = Modifier.fillMaxSize().weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Масштаб текста")
            }
            Column {
                Row {
                    IconButton(onClick = onDecreaseFontSizePress) {
                        Icon(
                            imageVector = Icons.Filled.ZoomOut,
                            contentDescription = "Уменьшить размер шрифта",
                        )
                    }
                    IconButton(onClick = onResetFontSizePress) {
                        Icon(
                            imageVector = Icons.Filled.YoutubeSearchedFor,
                            contentDescription = "Сбросить размер шрифта",
                        )
                    }
                    IconButton(onClick = onIncreaseFontSizePress) {
                        Icon(
                            imageVector = Icons.Filled.ZoomIn,
                            contentDescription = "Увеличить размер шрифта",
                        )
                    }
                }
            }
        }
        Divider(modifier = Modifier.fillMaxWidth())
    }
}