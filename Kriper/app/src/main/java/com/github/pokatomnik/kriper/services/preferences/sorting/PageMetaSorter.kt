package com.github.pokatomnik.kriper.services.preferences.sorting

import com.github.pokatomnik.kriper.domain.PageMeta

interface PageMetaSorter {
    val id: String
    val title: String
    fun compare(a: PageMeta, b: PageMeta): Int
}