package com.github.pokatomnik.kriper.services.index

import android.graphics.drawable.Drawable
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.Tag
import com.github.pokatomnik.kriper.ext.uppercaseFirst

data class TagGroup(
    private val pageMetaMap: Map<String, PageMeta>,
    val tagGroupName: String,
    private val tagGroupSource: Map<String, Tag>,
    private val getDrawableByTagName: (tagName: String) -> Drawable?,
) {
    private val tagsByName = mutableMapOf<String, TagContents>()

    val tagNames: Collection<String> by lazy {
        tagGroupSource.keys.sortedWith { a, b -> a.compareTo(b) }
    }

    val shortIntro by lazy {
        tagNames.joinToString(" ") { "#${it.uppercaseFirst()}" }
    }

    fun getTagContentsByName(tagName: String): TagContents =
        tagsByName[tagName] ?: tagGroupSource[tagName]?.let { tag ->
            TagContents(
                tagName = tagName,
                pageMetaMap = pageMetaMap,
                tag = tag,
                getDrawableByTagName = getDrawableByTagName
            ).apply {
                tagsByName[tagName] = this
            }
        } ?: TagContents(
            tagName = "",
            pageMetaMap = pageMetaMap,
            tag = Tag(
                tagName = "",
                pages = mutableSetOf()
            ),
            getDrawableByTagName = getDrawableByTagName,
        )
}
