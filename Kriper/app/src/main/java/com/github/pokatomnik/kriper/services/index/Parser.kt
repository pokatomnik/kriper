package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.domain.Index
import com.github.pokatomnik.kriper.services.index.builders.getDateCreatedISO
import com.github.pokatomnik.kriper.services.index.builders.getPageMetaList
import com.github.pokatomnik.kriper.services.index.builders.getTagsMap
import com.google.gson.JsonParser
import java.time.Instant

internal fun parseFromString(source: String): Index {
    return try {
        val jsonElement = JsonParser.parseString(source)

        val pageMeta = getPageMetaList(jsonElement)
        val tagGroupsMap = getTagsMap(jsonElement)
        val dateCreatedISO = getDateCreatedISO(jsonElement)

        Index(
            pageMeta = pageMeta,
            tagsMap = tagGroupsMap,
            dateCreatedISO = dateCreatedISO,
        )
    } catch (e: Exception) {
        Index(
            pageMeta = mapOf(),
            tagsMap = mapOf(),
            dateCreatedISO = Instant.now(),
        )
    }
}
