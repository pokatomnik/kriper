package com.github.pokatomnik.kriper.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING

@Composable
fun SectionDivider() {
    Divider(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = SMALL_PADDING.dp)
    )
}