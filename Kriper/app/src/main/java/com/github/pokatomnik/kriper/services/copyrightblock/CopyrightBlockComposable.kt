package com.github.pokatomnik.kriper.services.copyrightblock

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun rememberCopyrightBlockService(): CopyrightBlock {
    return hiltViewModel<CopyrightBlockViewModel>().copyrightBlock
}
