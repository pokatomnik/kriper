package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.Tag

class TagContents(
    val tagName: String,
    private val pageMetaMap: Map<String, PageMeta>,
    private val tag: Tag
) {
    val shortIntro by lazy {
        pageNames.joinToString(", ")
    }

    val pageNames: Collection<String>
        get() = tag.pages.sortedWith { a, b -> a.compareTo(b) }

    fun getPageByTitle(pageTitle: String): PageMeta? = pageMetaMap[pageTitle]
}