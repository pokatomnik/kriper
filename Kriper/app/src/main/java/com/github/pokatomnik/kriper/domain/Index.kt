package com.github.pokatomnik.kriper.domain

import java.time.Instant

class Index(
    val pageMeta: Map<String, PageMeta>,
    val tagsMap: Map<String, Map<String, Tag>>,
    val dateCreatedISO: Instant,
)