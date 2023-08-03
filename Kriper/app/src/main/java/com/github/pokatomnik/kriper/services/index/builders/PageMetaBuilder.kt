package com.github.pokatomnik.kriper.services.index.builders

import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.UncheckedDate
import com.google.gson.JsonElement

private fun getPageMetaItem(pageMetaElement: JsonElement): PageMeta {
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
            acc.apply { add(current.asString) }
        }

    val pageMeta = PageMeta(
        storyId = pageMetaElement.asJsonObject["storyId"].asString,
        title = pageMetaElement.asJsonObject["title"].asString,
        authorNickname = pageMetaElement.asJsonObject["authorNickname"].asString,
        authorRealName = try {
            pageMetaElement.asJsonObject["authorRealName"].asString
        } catch (e: Exception) { null },
        dateCreated = dateCreated,
        numberOfViews = pageMetaElement.asJsonObject["numberOfViews"].asInt,
        readingTimeMinutes = pageMetaElement.asJsonObject["readingTimeMinutes"].asFloat,
        source = try {
            pageMetaElement.asJsonObject["source"].asString
        } catch (e: Exception) {
            null
        },
        webpageURL = pageMetaElement.asJsonObject["webpageURL"].asString,
        rating = pageMetaElement.asJsonObject["rating"].asInt,
        tags = tags,
        seeAlso = seeAlso,
        images = images,
        videos = videos,
        gold = pageMetaElement.asJsonObject["gold"].asBoolean
    )

    return pageMeta
}

fun getPageMetaList(jsonElement: JsonElement): Map<String, PageMeta> {
    val pageMetaList = mutableMapOf<String, PageMeta>()

    val pageMetaJSONObject = jsonElement.asJsonObject["pageMeta"].asJsonObject
    for ((storyId, pageMetaElement) in pageMetaJSONObject.entrySet()) {
        pageMetaList[storyId] = getPageMetaItem(pageMetaElement)
    }

    return pageMetaList
}