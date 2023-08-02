package com.github.pokatomnik.kriper.screens.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING

@Composable
fun HomeHorizontalSpacerSmall() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(SMALL_PADDING.dp)
    )
}

@Composable
fun HomeHorizontalSpacerLarge() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(LARGE_PADDING.dp)
    )
}