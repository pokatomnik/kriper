package com.github.pokatomnik.kriper.services.preferences

interface PreferencesIntValue {
    fun write(value: Int)
    fun read(defaultValue: Int): Int
}

interface PreferencesStringValue {
    fun write(value: String)
    fun read(defaultValue: String): String
}

interface PreferencesBooleanValue {
    fun write(value: Boolean)
    fun read(defaultValue: Boolean): Boolean
}