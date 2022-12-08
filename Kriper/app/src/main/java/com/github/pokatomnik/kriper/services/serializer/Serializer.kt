package com.github.pokatomnik.kriper.services.serializer

interface Serializer {
    fun serialize(source: String): String
    fun parse(serialized: String): String
}