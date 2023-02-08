package com.github.pokatomnik.kriper.services.index

import android.graphics.drawable.Drawable
import com.github.pokatomnik.kriper.services.contentreader.ContentReaderService
import com.github.pokatomnik.kriper.domain.Index
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.Tag
import com.github.pokatomnik.kriper.ext.valuableChars
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.random.Random

class Content(
    private val contentReaderService: ContentReaderService,
    private val index: Index,
) {
    val groupNames: Collection<String>
        get() = index.tagsMap.keys.sortedWith { a, b -> a.compareTo(b) }

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
            tagGroupSource = tagGroupSource,
            getDrawableByTagName = { getTagImage(it) }
        )
    }

    val selections = Selections(index)

    fun getPageMetaByName(storyTitle: String): PageMeta? = index.pageMeta[storyTitle]

    fun getRandomPageMeta() = selections
        .allStoryTitles
        .random(Random(System.currentTimeMillis()))
        .let {
            index.pageMeta[it]
        }

    private val getTagImage = object : (String) -> Drawable? {
        private val allowedChars = "abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя"
            .let { it + it.uppercase() }
            .let { "$it " }
            .plus("0123456789".split("").toSet())
        override fun invoke(tagName: String): Drawable? {
            val tagNameClean = tagName.lowercase().filter { allowedChars.contains(it) }
            return contentReaderService.getDrawable("tag-icons/$tagNameClean.png")
        }
    }

    fun getTagGroupByName(tagGroupName: String): TagGroup =
        tagContentsMap[tagGroupName] ?: index.tagsMap[tagGroupName]?.let { tagGroupSource ->
            TagGroup(
                pageMetaMap = index.pageMeta,
                tagGroupName = tagGroupName,
                tagGroupSource = tagGroupSource,
                getDrawableByTagName = { getTagImage(it) }
            ).apply {
                tagContentsMap[tagGroupName] = this
            }
        } ?: TagGroup(
            pageMetaMap = index.pageMeta,
            tagGroupName = "",
            tagGroupSource = mapOf(),
            getDrawableByTagName = { getTagImage(it) }
        )

    suspend fun getStoryMarkdown(storyTitle: String) =
        withContext(Dispatchers.IO + SupervisorJob()) {
            try {
                val pageMeta = index.pageMeta[storyTitle] ?: throw IOException("No content for page meta with title $storyTitle")
                contentReaderService.getTextContent("content/${pageMeta.contentId}.md")
            } catch (e: IOException) {
                ""
            }
        }

    private fun matchSearchStr(searchItem: String, sample: String): Boolean {
        return searchItem.lowercase().contains(sample)
    }

    suspend fun search(rawSearchString: String) =
        withContext(Dispatchers.Default + SupervisorJob()) {
            val searchStringLower = rawSearchString.trim().lowercase()

            val tagGroupsFound = mutableMapOf<String, TagGroup>()
            val tagContentItemsFound = mutableMapOf<String, TagContents>()
            val pageMetaFound = mutableMapOf<String, PageMeta>()

            for (currentTagGroupName in groupNames) {
                val currentTagGroup = getTagGroupByName(currentTagGroupName)
                if (matchSearchStr(currentTagGroupName, searchStringLower)) {
                    tagGroupsFound[currentTagGroupName] = currentTagGroup
                }

                for (currentTagName in currentTagGroup.tagNames) {
                    val currentTag = currentTagGroup.getTagContentsByName(currentTagName)
                    if (matchSearchStr(currentTagName, searchStringLower)) {
                        tagContentItemsFound[currentTagName] = currentTag
                    }

                    for (currentPageName in currentTag.pageNames) {
                        val currentPageMeta = currentTag.getPageByTitle(currentPageName)
                        val titleMatch = matchSearchStr(currentPageName, searchStringLower)
                        val authorNicknameMatch = currentPageMeta?.let {
                            matchSearchStr(it.authorNickname, searchStringLower)
                        } ?: false
                        val authorRealNameMatch = currentPageMeta?.authorRealName?.let {
                            matchSearchStr(it, searchStringLower)
                        } ?: false
                        if (titleMatch || authorNicknameMatch || authorRealNameMatch) {
                            currentPageMeta?.let { pageMetaFound[currentPageName] = currentPageMeta }
                        }
                    }
                }
            }

            val tagGroups = tagGroupsFound.values.sortedWith { a, b ->
                a.tagGroupName.valuableChars().lowercase().compareTo(
                    b.tagGroupName.valuableChars().lowercase()
                )
            }
            val tagContentItems = tagContentItemsFound.values.sortedWith { a, b ->
                a.tagName.valuableChars().lowercase().compareTo(
                    b.tagName.valuableChars().lowercase()
                )
            }
            val pageMeta = pageMetaFound.values.sortedWith { a, b ->
                a.title.valuableChars().lowercase().compareTo(
                    b.title.valuableChars().lowercase()
                )
            }

            SearchResults(
                tagGroups = tagGroups,
                tagContentItems = tagContentItems,
                pageMeta = pageMeta
            )
        }
}