package com.github.pokatomnik.kriper.services.index.builders

import com.github.pokatomnik.kriper.domain.Tag
import com.google.gson.JsonElement

fun getTag(tagElement: JsonElement): Tag {
    val tagObject = tagElement.asJsonObject
    val tagName = tagObject.getAsJsonPrimitive("tagName").asString
    val tagPagesArray = tagObject.getAsJsonArray("pages")
    val pages = mutableSetOf<String>()

    for (pageElement in tagPagesArray) {
        pages.add(pageElement.asString)
    }

    return Tag(tagName, pages)
}

fun getTagMap(groupElement: JsonElement): Map<String, Tag> {
    val tagMap = mutableMapOf<String, Tag>()
    val groupObject = groupElement.asJsonObject

    for ((tagName, tagElement) in groupObject.entrySet()) {
        tagMap[tagName] = getTag(tagElement)
    }

    return tagMap
}

fun getTagsMap(rootJSONElement: JsonElement): Map<String, Map<String, Tag>> {
    val tagsMap = mutableMapOf<String, Map<String, Tag>>()
    val tagsMapObject = rootJSONElement.asJsonObject.getAsJsonObject("tagsMap")

    for ((groupName, groupElement) in tagsMapObject.entrySet()) {
        tagsMap[groupName] = getTagMap(groupElement)
    }

    return tagsMap

}