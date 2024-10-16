package com.github.pokatomnik.kriper.services.index

import android.graphics.drawable.Drawable
import android.net.Uri
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

    suspend fun getStoryShortDescriptionById(storyId: String) =
        withContext(Dispatchers.IO + SupervisorJob()) {
            try {
                val pageMeta = index.pageMeta[storyId]
                    ?: throw IOException("No short description for page meta with id $storyId")
                contentReaderService.getTextContent("content/${pageMeta.storyId}.short.md")
            } catch (e: IOException) {
                ""
            }
        }

    suspend fun findStoryByPathMatch(uri: Uri) = withContext(Dispatchers.Main + SupervisorJob()) {
        index.pageMeta.values.find { currentPageMeta ->
            val currentURI = Uri.parse(currentPageMeta.webpageURL)
            currentURI.path == uri.path
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
    val search = object : suspend (String, Boolean) -> SearchResults {
        private val quickSearchResults = mutableMapOf<String, SearchResults>()

        private val fullTextSearchResults = mutableMapOf<String, SearchResults>()

        private val replacements = mapOf('ё' to 'е')

        private fun String.applyReplacements(): String {
            return replacements.entries.fold(this) { acc, (from, to) ->
                return acc.replace(from, to)
            }
        }

        private fun matchSearchStr(
            textContent: String,
            sample: String,
            applyReplacements: Boolean
        ): Boolean {
            val actualContent = if (applyReplacements) textContent.applyReplacements() else textContent
            val actualSample = if (applyReplacements) sample.applyReplacements() else sample
            return actualContent.lowercase().contains(actualSample)
        }

        override suspend fun invoke(rawSearchString: String, searchInContent: Boolean): SearchResults {
            return withContext(Dispatchers.IO + SupervisorJob()) {
                val searchStringLower = rawSearchString.trim().lowercase()

                val selectedMemoizedResultsMap = if (searchInContent) fullTextSearchResults else quickSearchResults
                val resultsFound = selectedMemoizedResultsMap[searchStringLower]
                if (resultsFound != null) return@withContext resultsFound

                val tagGroupsFound = mutableMapOf<String, TagGroup>()
                val tagContentItemsFound = mutableMapOf<String, TagContents>()
                val pageMetaFound = mutableMapOf<String, PageMeta>()

                // Go through tags and tag groups, checking all names
                for (currentTagGroupName in groupNames) {
                    val currentTagGroup = getTagGroupByName(currentTagGroupName)
                    if (matchSearchStr(currentTagGroupName, searchStringLower, applyReplacements = true)) {
                        tagGroupsFound[currentTagGroupName] = currentTagGroup
                    }

                    for (currentTagName in currentTagGroup.tagNames) {
                        val currentTag = currentTagGroup.getTagContentsByName(currentTagName)
                        if (matchSearchStr(currentTagName, searchStringLower, applyReplacements = true)) {
                            tagContentItemsFound[currentTagName] = currentTag
                        }
                    }
                }

                // Go through all page meta, check story titles, author names
                for (currentPageMeta in index.pageMeta.values) {
                    val titleMatch = matchSearchStr(currentPageMeta.title, searchStringLower, applyReplacements = true)
                    val authorNicknameMatch = matchSearchStr(
                        textContent = currentPageMeta.authorNickname,
                        sample = searchStringLower,
                        applyReplacements = true
                    )
                    val authorRealNameMatch = currentPageMeta.authorRealName?.let {
                        matchSearchStr(
                            textContent = it,
                            sample = searchStringLower,
                            applyReplacements = true,
                        )
                    } ?: false

                    if (titleMatch || authorNicknameMatch || authorRealNameMatch) {
                        pageMetaFound[currentPageMeta.storyId] = currentPageMeta
                        continue
                    }
                    if (!searchInContent) continue

                    val currentContent = getStoryMarkDownByStoryId(currentPageMeta.storyId)
                    val contentMatch = matchSearchStr(
                        textContent = currentContent,
                        sample = searchStringLower,
                        applyReplacements = false
                    )
                    if (contentMatch) {
                        pageMetaFound[currentPageMeta.storyId] = currentPageMeta
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

                val searchResults = SearchResults(
                    tagGroups = tagGroups,
                    tagContentItems = tagContentItems,
                    pageMeta = pageMeta
                )

                selectedMemoizedResultsMap[searchStringLower] = searchResults

                return@withContext searchResults
            }
        }

    }
}