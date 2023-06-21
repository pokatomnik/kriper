package com.github.pokatomnik.kriper.services.index

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.Tag
import com.google.accompanist.drawablepainter.rememberDrawablePainter

class TagContents(
    val tagName: String,
    private val pageMetaMap: Map<String, PageMeta>,
    private val tag: Tag,
    private val getDrawableByTagName: (tagName: String) -> Drawable?
) {
    val shortIntro by lazy {
        storyIds.joinToString(", ")
    }

    private val drawable by lazy { getDrawableByTagName(tagName) }

    val storyIds: Collection<String>
        get() = tag.storyIds.sortedWith { storyId1, storyId2 ->
            val title1 = pageMetaMap[storyId1]?.title ?: ""
            val title2 = pageMetaMap[storyId2]?.title ?: ""
            title1.compareTo(title2)
        }

    fun getPageByStoryId(storyId: String): PageMeta? = pageMetaMap[storyId]

    @Composable
    fun image(): Painter {
        return rememberDrawablePainter(drawable = drawable)
    }
}