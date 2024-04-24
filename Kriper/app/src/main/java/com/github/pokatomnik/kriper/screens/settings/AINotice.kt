package com.github.pokatomnik.kriper.screens.settings

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AINotice(
    navigateToAIPowers: () -> Unit,
) {
    TextButton(onClick = navigateToAIPowers) {
        Text("AI в приложении")
    }
}