package com.github.pokatomnik.kriper.services.index.builders

import com.github.pokatomnik.kriper.domain.Top
import com.google.gson.JsonElement

fun getTop(jsonElement: JsonElement): Top {
    val topObject = jsonElement
        .asJsonObject
        .getAsJsonObject("top")
        .asJsonObject

    val weekTop = topObject
        .getAsJsonArray("weekTop")
        .map { it.asString }
        .toSet()
    val monthTop = topObject
        .getAsJsonArray("monthTop")
        .map { it.asString }
        .toSet()
    val yearTop = topObject
        .getAsJsonArray("yearTop")
        .map { it.asString }
        .toSet()
    val allTheTimeTop = topObject
        .getAsJsonArray("allTheTimeTop")
        .map { it.asString }
        .toSet()

    return Top(
        weekTop = weekTop,
        monthTop = monthTop,
        yearTop = yearTop,
        allTheTimeTop = allTheTimeTop
    )
}