package com.github.pokatomnik.kriper.services.index

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

@Composable
fun IndexServiceReadiness(
    wip: @Composable () -> Unit = {},
    done: @Composable (indexService: IndexService) -> Unit = {},
) {
    val indexService = hiltViewModel<IndexServiceViewModel>().indexService
    val (prepared, setPrepared) = remember { mutableStateOf(indexService.prepared) }

    LaunchedEffect(Unit) {
        if (!indexService.prepared) {
            withContext(Dispatchers.Default + SupervisorJob()) {
                indexService.prepareIndex()
                setPrepared(true)
            }
        }
    }

    if (prepared) done(indexService) else wip()
}