package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.contentreader.ContentReaderService
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.io.IOException

class Content(
    private val pageMetaMap: Map<String, PageMeta>,
    private val contentReaderService: ContentReaderService,
    private val contentSource: Map<String, Map<String, Tag>>
) {
    val groupNames: Collection<String>
        get() = contentSource.keys

    private val tagContentsMap = mutableMapOf<String, TagGroup>()

    fun getTagGroupByName(tagGroupName: String): TagGroup =
        tagContentsMap[tagGroupName] ?: contentSource[tagGroupName]?.let { tagGroupSource ->
            TagGroup(
                pageMetaMap = pageMetaMap,
                tagGroupName = tagGroupName,
                tagGroupSource = tagGroupSource
            ).apply {
                tagContentsMap[tagGroupName] = this
            }
        } ?: TagGroup(
            pageMetaMap = pageMetaMap,
            tagGroupName = "",
            tagGroupSource = mapOf()
        )

    suspend fun getStoryMarkdown(storyTitle: String) =
        withContext(Dispatchers.IO + SupervisorJob()) {
            try {
                val pageMeta = pageMetaMap[storyTitle] ?: throw IOException("No content for page meta with title $storyTitle")
                contentReaderService.readContents("content/${pageMeta.contentId}.md")
            } catch (e: IOException) {
                ""
            }
        }
}