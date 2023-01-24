package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.domain.PageMeta

data class SearchResults(
    val tagGroups: Collection<TagGroup> = listOf(),
    val tagContentItems: Collection<TagContents> = listOf(),
    val pageMeta: Collection<PageMeta> = listOf(),
)