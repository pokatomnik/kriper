package com.github.pokatomnik.kriper.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageContainer(
    navigationBar: @Composable () -> Unit = { KriperNavBottomBar() },
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = topBar,
        bottomBar = navigationBar
    ) {
        Surface(
            modifier = Modifier.fillMaxSize().padding(it),
            color = MaterialTheme.colorScheme.background
        ) { content() }
    }
}
