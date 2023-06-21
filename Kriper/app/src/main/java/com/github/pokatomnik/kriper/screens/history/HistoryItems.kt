package com.github.pokatomnik.kriper.screens.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.services.db.rememberKriperDatabase
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

@Composable
fun HistoryItems(
    content: @Composable (historyItems: List<PageMeta>) -> Unit
) {
    val historyDAO = rememberKriperDatabase().historyDAO()

    IndexServiceReadiness { indexService ->
        val (pageMeta, setPageMeta) = remember {
            mutableStateOf<List<PageMeta>?>(null)
        }

        LaunchedEffect(Unit) {
            val historyItems = withContext(Dispatchers.IO + SupervisorJob()) {
                historyDAO.getAll()
            }

            val computedPageMeta = withContext(Dispatchers.Default + SupervisorJob()) {
                historyItems.fold(mutableListOf<PageMeta>()) { acc, current ->
                    acc.apply {
                        indexService.content.getPageMetaByStoryId(current.id)?.let { pageMeta ->
                            add(pageMeta)
                        }
                    }
                }
            }

            setPageMeta(computedPageMeta)
        }

        pageMeta?.let {
            content(it)
        }
    }

}