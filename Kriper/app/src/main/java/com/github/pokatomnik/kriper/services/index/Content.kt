package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.contentreader.ContentReaderService
import com.github.pokatomnik.kriper.domain.Index
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.io.IOException

class Content(
    private val contentReaderService: ContentReaderService,
    private val index: Index,
) {
    val groupNames: Collection<String>
        get() = index.tagsMap.keys

    private val tagContentsMap = mutableMapOf<String, TagGroup>()

    val allTagsGroup: TagGroup by lazy {
        val tagGroupSource = index.tagsMap.entries
            .fold(mutableMapOf<String, Tag>()) { allTagsGroup, (_, currentTagGroup) ->
                allTagsGroup.apply {
                    currentTagGroup.forEach { (currentKey, currentTag) ->
                        allTagsGroup[currentKey] = currentTag
                    }
                }
            }
        TagGroup(
            pageMetaMap = index.pageMeta,
            tagGroupName = "ВСЕ",
            tagGroupSource = tagGroupSource
        )
    }

    val allStoryTitles: Collection<String> by lazy {
        index.pageMeta.keys.sortedWith { a, b -> a.compareTo(b) }
    }

    fun getPageMetaByName(storyTitle: String): PageMeta? = index.pageMeta[storyTitle]

    fun getTagGroupByName(tagGroupName: String): TagGroup =
        tagContentsMap[tagGroupName] ?: index.tagsMap[tagGroupName]?.let { tagGroupSource ->
            TagGroup(
                pageMetaMap = index.pageMeta,
                tagGroupName = tagGroupName,
                tagGroupSource = tagGroupSource
            ).apply {
                tagContentsMap[tagGroupName] = this
            }
        } ?: TagGroup(
            pageMetaMap = index.pageMeta,
            tagGroupName = "",
            tagGroupSource = mapOf()
        )

    suspend fun getStoryMarkdown(storyTitle: String) =
        withContext(Dispatchers.IO + SupervisorJob()) {
            try {
                val pageMeta = index.pageMeta[storyTitle] ?: throw IOException("No content for page meta with title $storyTitle")
                contentReaderService.readContents("content/${pageMeta.contentId}.md")
            } catch (e: IOException) {
                ""
            }
        }
}