package com.github.pokatomnik.kriper.services.index.builders

import com.google.gson.JsonElement

fun getNY2024(jsonElement: JsonElement): Set<String> {
    val ny2024StoryIds = jsonElement
        .asJsonObject
        .getAsJsonArray("ny2024")
    return ny2024StoryIds.map { it.asString }.toSet()
}