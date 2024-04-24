package com.github.pokatomnik.kriper.screens.settings

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LegalWarningButton(
    navigateToAbout: () -> Unit,
) {
    TextButton(onClick = navigateToAbout) {
        Text("О приложении")
    }
}