package com.github.pokatomnik.kriper.services.index.builders

import com.google.gson.JsonElement
import java.time.Instant

fun getDateCreatedISO(jsonElement: JsonElement): Instant {
    val dateStr = jsonElement.asJsonObject.getAsJsonPrimitive("dateCreatedISO").asString
    return Instant.parse(dateStr)
}