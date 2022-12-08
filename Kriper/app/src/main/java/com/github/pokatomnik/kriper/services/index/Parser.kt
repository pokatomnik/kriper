package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.domain.Index
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.Tag
import com.github.pokatomnik.kriper.domain.UncheckedDate
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.time.Instant

const val ALL_TAGS_GROUP = "ВСЕ"

internal fun parseFromString(source: String): Index {
    return try {
        val jsonElement = JsonParser.parseString(source)

        val tagsMap = getTagsMap(jsonElement)
        val pageMeta = getPageMeta(jsonElement) { pageMeta ->
            pageMeta.tags.forEach { tagName ->
                tagsMap[ALL_TAGS_GROUP]
                    ?.get(tagName)
                    ?.pages
                    ?.set(pageMeta.title, pageMeta)
            }
        }
        val dateCreatedISO = getDateCreatedISO(jsonElement)

        Index(
            pageMeta = pageMeta,
            tagsMap = tagsMap,
            dateCreatedISO = dateCreatedISO,
        )
    } catch (e: Exception) {
        Index(
            pageMeta = listOf(),
            tagsMap = mapOf(),
            dateCreatedISO = Instant.now(),
        )
    }
}

private fun getPageMeta(
    jsonElement: JsonElement,
    onPageMetaReady: (pageMeta: PageMeta) -> Unit,
): List<PageMeta> {
    val pageMetaList = mutableListOf<PageMeta>()

    val pageMetaJSONArray = jsonElement.asJsonObject["pageMeta"].asJsonArray
    for (pageMetaElement in pageMetaJSONArray) {

        val dateCreatedObject = pageMetaElement.asJsonObject["dateCreated"].asJsonObject
        val dateCreated = UncheckedDate(
            year = dateCreatedObject["year"].asInt,
            month = dateCreatedObject["month"].asInt,
            day = dateCreatedObject["day"].asInt
        )

        val tags =
            pageMetaElement.asJsonObject["tags"].asJsonArray.fold(mutableSetOf<String>()) { acc, current ->
                acc.apply { add(current.asString) }
            }

        val seeAlso =
            pageMetaElement.asJsonObject["seeAlso"].asJsonArray.fold(mutableSetOf<String>()) { acc, current ->
                acc.apply { add(current.asString) }
            }

        val images =
            pageMetaElement.asJsonObject["images"].asJsonArray.fold(mutableSetOf<String>()) { acc, current ->
                acc.apply { add(current.asString) }
            }

        val videos =
            pageMetaElement.asJsonObject["videos"].asJsonArray.fold(mutableSetOf<String>()) { acc, current ->
                acc.apply { add(current.toString()) }
            }

        val pageMeta = PageMeta(
            contentId = pageMetaElement.asJsonObject["contentId"].asString,
            title = pageMetaElement.asJsonObject["title"].asString,
            authorNickname = pageMetaElement.asJsonObject["authorNickname"].asString,
            dateCreated = dateCreated,
            numberOfViews = pageMetaElement.asJsonObject["numberOfViews"].asInt,
            readingTimeMinutes = pageMetaElement.asJsonObject["readingTimeMinutes"].asInt,
            source = try {
                pageMetaElement.asJsonObject["source"].asString
            } catch (e: Exception) {
                null
            },
            rating = pageMetaElement.asJsonObject["rating"].asInt,
            tags = tags,
            seeAlso = seeAlso,
            images = images,
            videos = videos,
        )
        onPageMetaReady(pageMeta)
        pageMetaList.add(pageMeta)
    }

    return pageMetaList
}

private fun getTagsMap(rootJSONElement: JsonElement): Map<String, MutableMap<String, Tag>> {
    val tagsMap = mutableMapOf<String, MutableMap<String, Tag>>()
    val jsonObject = rootJSONElement.asJsonObject["tagsMap"]

    for ((groupName, groupJSONValue) in jsonObject.asJsonObject.entrySet()) {
        for (tagJSONItem in groupJSONValue.asJsonArray) {
            val tagJSONObject = tagJSONItem.asJsonObject
            val tagName = tagJSONObject["tagName"].asString
            val tag = Tag(tagName, mutableMapOf())

            tagsMap[groupName] = tagsMap[groupName]?.apply {
                set(tagName, tag)
            } ?: mutableMapOf(tagName to tag)
            tagsMap[ALL_TAGS_GROUP] = tagsMap[ALL_TAGS_GROUP]?.apply {
                set(tagName, tag)
            } ?: mutableMapOf(tagName to tag)
        }
    }

    return tagsMap
}

private fun getDateCreatedISO(jsonElement: JsonElement): Instant {
    val dateStr = jsonElement.asJsonObject.getAsJsonPrimitive("dateCreatedISO").asString
    return Instant.parse(dateStr)
}