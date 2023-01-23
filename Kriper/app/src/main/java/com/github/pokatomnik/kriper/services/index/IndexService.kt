package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.services.contentreader.ContentReaderService
import com.github.pokatomnik.kriper.domain.Index
import java.time.Instant

class IndexService(private val contentReaderService: ContentReaderService) {
    private var _index: Index? = null

    private val index: Index
        get() = _index ?: prepareIndex()

    val prepared: Boolean
        get() = _index != null

    @Synchronized
    fun prepareIndex(): Index {
        val indexJSONContent = contentReaderService
            .readContents("content/index.json")
        return parseFromString(indexJSONContent).apply { _index = this }
    }

    val dateCreatedISO: Instant
        get() = index.dateCreatedISO

    val content by lazy {
        Content(
            contentReaderService = contentReaderService,
            index = index,
        )
    }
}
