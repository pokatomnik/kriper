package com.github.pokatomnik.kriper.services.index

import android.graphics.drawable.Drawable
import com.github.pokatomnik.kriper.domain.Index
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.Tag
import com.github.pokatomnik.kriper.ext.valuableChars
import com.github.pokatomnik.kriper.services.contentreader.ContentReaderService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
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

    fun getPageMetaByStoryId(storyId: String): PageMeta? = index.pageMeta[storyId]

    fun getRandomPageMeta(): PageMeta? {
        val pageMetaItems = index.pageMeta.values
        if (pageMetaItems.isEmpty()) {
            return null
        }
        return index.pageMeta.values.random(Random(System.currentTimeMillis()))
    }

    private val getTagImage = object : (String) -> Drawable? {
        private val allowedChars = "abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя"
            .let { it + it.uppercase() }
            .plus(" _-")
            .plus("0123456789".split("").toSet())

        override fun invoke(tagName: String): Drawable? {
            val tagNameClean = tagName.lowercase().filter { allowedChars.contains(it) }
            return try {
                contentReaderService.getDrawable("tag-icons/$tagNameClean.png")
            } catch (e: IOException) {
                contentReaderService.getDrawable("tag-icons/$tagNameClean.jpg")
            }
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

    suspend fun getStoryMarkDownByStoryId(storyId: String) =
        withContext(Dispatchers.IO + SupervisorJob()) {
            try {
                val pageMeta = index.pageMeta[storyId]
                    ?: throw IOException("No content for page meta with id $storyId")
                contentReaderService.getTextContent("content/${pageMeta.storyId}.md")
            } catch (e: IOException) {
                ""
            }
        }

    /**
     * It seems there are too many authors to precompute all author-to-stories
     * matching, so this will be computed on demand (but with memoization)
     */
    val getAllStoriesByAuthor = object : suspend (String) -> Collection<PageMeta> {
        private val authorRealNameToPageMetaMapping =
            ConcurrentHashMap<String, Collection<PageMeta>>()

        override suspend fun invoke(authorRealName: String): Collection<PageMeta> =
            authorRealNameToPageMetaMapping[authorRealName]
                ?: withContext(Dispatchers.Default + SupervisorJob()) {
                    index.pageMeta.values.filter { pageMeta ->
                        pageMeta.authorRealName == authorRealName
                    }.apply {
                        authorRealNameToPageMetaMapping[authorRealName] = this
                    }
                }
    }

    /**
     * Searches for pageMeta, tags or tag groups using provided search string.
     * Please note, searching process works on CPU thread, not in the render one.
     * PageMeta fields used for search are:
     * - authorNickname
     * - authorRealName
     * - title
     * Only "valuable" chars are used for searching: cyrillic and latin alphabet and numbers.
     * Search is case insensitive.
     */
    val search = object : suspend (String) -> SearchResults {
        private fun matchSearchStr(searchItem: String, sample: String): Boolean {
            return searchItem.lowercase().contains(sample)
        }

        override suspend fun invoke(rawSearchString: String): SearchResults {
            return withContext(Dispatchers.Default + SupervisorJob()) {
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

                        for (currentStoryId in currentTag.storyIds) {
                            val currentPageMeta = currentTag.getPageByStoryId(currentStoryId)
                            val titleMatch = currentPageMeta?.let {
                                matchSearchStr(it.title, searchStringLower)
                            } ?: false
                            val authorNicknameMatch = currentPageMeta?.let {
                                matchSearchStr(it.authorNickname, searchStringLower)
                            } ?: false
                            val authorRealNameMatch = currentPageMeta?.authorRealName?.let {
                                matchSearchStr(it, searchStringLower)
                            } ?: false
                            if (titleMatch || authorNicknameMatch || authorRealNameMatch) {
                                currentPageMeta?.let { pageMetaFound.put(it.storyId, it) }
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

    }
}