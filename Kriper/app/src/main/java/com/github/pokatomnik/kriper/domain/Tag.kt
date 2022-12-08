package com.github.pokatomnik.kriper.domain

data class Tag(val tagName: String, val pages: MutableMap<String, PageMeta>)
