package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.Tag

class TagContents(
    val tagName: String,
    private val pageMetaMap: Map<String, PageMeta>,
    private val tag: Tag
) {
    val pageNames: Collection<String>
        get() = tag.pages

    fun getPageByTitle(pageTitle: String): PageMeta? = pageMetaMap[pageTitle]
}