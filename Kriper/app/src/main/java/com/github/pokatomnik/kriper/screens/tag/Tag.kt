package com.github.pokatomnik.kriper.screens.tag

import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle

@Composable
fun Tag(
    tagTitle: String,
    onNavigateToTagGroups: () -> Unit
) {
    PageContainer(
        priorButton = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        header = {
            PageTitle(title = "Тег $tagTitle")
        }
    ) {
        Button(onClick = onNavigateToTagGroups) {
            Text("This is tag screen. Name passed: $tagTitle")
        }
    }
}