package com.github.pokatomnik.kriper.screens.taggroups

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.ui.components.PageContainer

@Composable
fun TagGroups(onNavigateToGroup: (tagName: String) -> Unit) {
    PageContainer {
        Button(onClick = { onNavigateToGroup("Example") }) {
            Text("This is tag groups screen")
        }
    }
}