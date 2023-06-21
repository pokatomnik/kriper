package com.github.pokatomnik.kriper.domain

data class PageMeta(
    /**
     * Identifier of the story
     */
    val storyId: String,
    val title: String,
    val authorNickname: String,
    val authorRealName: String?,
    val dateCreated: UncheckedDate,
    val numberOfViews: Int,
    val readingTimeMinutes: Float,
    val source: String?,
    val webpageURL: String,
    val rating: Int,
    val tags: Set<String>,
    val seeAlso: Set<String>,
    val images: Set<String>,
    val videos: Set<String>
) {
    val authorship: String
        get() = authorRealName ?: authorNickname
}