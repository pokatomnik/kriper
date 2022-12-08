package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.contentreader.ContentReaderService
import com.github.pokatomnik.kriper.domain.PageMeta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.io.IOException

data class Page(
    private val contentReaderService: ContentReaderService,
    val pageMeta: PageMeta
) {
    suspend fun readContent() = withContext(
        Dispatchers.IO + SupervisorJob()
    ) {
        try {
            contentReaderService.readContents(
                "content/${pageMeta.contentId}.md"
            )
        } catch (e: IOException) {
            ""
        }
    }
}