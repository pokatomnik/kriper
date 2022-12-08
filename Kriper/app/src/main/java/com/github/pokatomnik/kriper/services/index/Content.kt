package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.contentreader.ContentReaderService
import com.github.pokatomnik.kriper.domain.Tag

class Content(
    private val contentReaderService: ContentReaderService,
    private val contentSource:  Map<String, Map<String, Tag>>
) {
    val groupNames: Collection<String>
        get() = contentSource.keys

    private val tagContentsMap = mutableMapOf<String, TagGroup>()

    fun getTagGroupByName(tagGroupName: String): TagGroup =
        tagContentsMap[tagGroupName] ?: contentSource[tagGroupName]?.let { tagGroupSource ->
            TagGroup(
                contentReaderService = contentReaderService,
                tagGroupName = tagGroupName,
                tagGroupSource = tagGroupSource
            ).apply {
                tagContentsMap[tagGroupName] = this
            }
        } ?: TagGroup(
            contentReaderService = contentReaderService,
            tagGroupName = "",
            tagGroupSource = mapOf()
        )
}