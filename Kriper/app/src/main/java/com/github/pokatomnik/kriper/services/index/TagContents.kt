package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.contentreader.ContentReaderService
import com.github.pokatomnik.kriper.domain.Tag

class TagContents(
    private val contentReaderService: ContentReaderService,
    val tagName: String,
    private val tag: Tag
) {
    private val pagesByTitle = mutableMapOf<String, Page>()

    val pageNames: Collection<String>
        get() = tag.pages.keys

    fun getPageByTitle(pageTitle: String): Page? =
        pagesByTitle[pageTitle] ?: tag.pages[pageTitle]?.let { pageMeta ->
            Page(
                contentReaderService = contentReaderService,
                pageMeta = pageMeta,
            ).apply {
                pagesByTitle[pageTitle] = this
            }
        }
}