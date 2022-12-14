package com.github.pokatomnik.kriper.domain

data class PageMeta(
    val contentId: String,
    val title: String,
    val authorNickname: String,
    val dateCreated: UncheckedDate,
    val numberOfViews: Int,
    val readingTimeMinutes: Float,
    val source: String?,
    val rating: Int,
    val tags: Set<String>,
    val seeAlso: Set<String>,
    val images: Set<String>,
    val videos: Set<String>
)