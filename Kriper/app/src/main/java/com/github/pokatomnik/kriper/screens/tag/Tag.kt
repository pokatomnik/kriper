package com.github.pokatomnik.kriper.screens.tag

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Tag(
    tagTitle: String,
    onNavigateToTagGroups: () -> Unit
) {
    Button(onClick = onNavigateToTagGroups) {
        Text("This is tag screen. Name passed: $tagTitle")
    }
}