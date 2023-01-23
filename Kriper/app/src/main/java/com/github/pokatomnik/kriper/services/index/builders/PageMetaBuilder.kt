package com.github.pokatomnik.kriper.services.index.builders

import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.domain.UncheckedDate
import com.google.gson.JsonElement

fun getPageMetaItem(pageMetaElement: JsonElement): PageMeta {
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
        rating = pageMetaElement.asJsonObject["rating"].asInt,
        tags = tags,
        seeAlso = seeAlso,
        images = images,
        videos = videos,
    )

    return pageMeta
}

fun getPageMetaList(jsonElement: JsonElement): Map<String, PageMeta> {
    val pageMetaList = mutableMapOf<String, PageMeta>()

    val pageMetaJSONObject = jsonElement.asJsonObject["pageMeta"].asJsonObject
    for ((pageTitle, pageMetaElement) in pageMetaJSONObject.entrySet()) {
        pageMetaList[pageTitle] = getPageMetaItem(pageMetaElement)
    }

    return pageMetaList
}