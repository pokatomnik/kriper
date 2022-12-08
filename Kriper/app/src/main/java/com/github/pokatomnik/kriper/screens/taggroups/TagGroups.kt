package com.github.pokatomnik.kriper.screens.taggroups

import androidx.compose.foundation.layout.Row
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
fun TagGroups(onNavigateToGroup: (tagName: String) -> Unit) {
    PageContainer(
        priorButton = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        header = {
            PageTitle(title = "Группы тегов")
        }
    ) {
        Button(onClick = { onNavigateToGroup((0..10000).random().toString()) }) {
            Text("This is tag groups screen")
        }
    }
}