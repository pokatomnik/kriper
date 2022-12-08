package com.github.pokatomnik.kriper.screens.tag

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.ui.components.PageContainer

@Composable
fun Tag(
    tagTitle: String,
    onNavigateToTagGroups: () -> Unit
) {
    PageContainer {
        Button(onClick = onNavigateToTagGroups) {
            Text("This is tag screen. Name passed: $tagTitle")
        }
    }
}