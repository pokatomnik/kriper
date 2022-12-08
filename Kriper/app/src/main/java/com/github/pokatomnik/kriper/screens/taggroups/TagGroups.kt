package com.github.pokatomnik.kriper.screens.taggroups

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TagGroups(onNavigateToGroup: (tagName: String) -> Unit) {
    Button(onClick = { onNavigateToGroup((0..10000).random().toString()) }) {
        Text("This is tag groups screen")
    }
}