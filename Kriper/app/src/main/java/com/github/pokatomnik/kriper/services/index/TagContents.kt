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
        pageNames.joinToString(", ")
    }

    private val drawable by lazy { getDrawableByTagName(tagName) }

    val pageNames: Collection<String>
        get() = tag.pages.sortedWith { a, b -> a.compareTo(b) }

    fun getPageByTitle(pageTitle: String): PageMeta? = pageMetaMap[pageTitle]

    @Composable
    fun image(): Painter {
        return rememberDrawablePainter(drawable = drawable)
    }
}