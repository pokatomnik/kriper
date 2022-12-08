package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.contentreader.ContentReaderService
import com.github.pokatomnik.kriper.domain.Tag

class TagGroup(
    private val contentReaderService: ContentReaderService,
    val tagGroupName: String,
    private val tagGroupSource: Map<String, Tag>
) {
    private val tagsByName = mutableMapOf<String, TagContents>()

    val tagNames: Collection<String>
        get() = tagGroupSource.keys

    fun getTagContentsByName(tagName: String): TagContents =
        tagsByName[tagName] ?: tagGroupSource[tagName]?.let { tag ->
            TagContents(
                contentReaderService = contentReaderService,
                tagName = tagName,
                tag = tag
            ).apply {
                tagsByName[tagName] = this
            }
        } ?: TagContents(
            contentReaderService = contentReaderService,
            tagName = "",
            tag = Tag(
                tagName = "",
                pages = mutableMapOf()
            )
        )
}
