package com.github.pokatomnik.kriper.services.preferences.search

import android.content.SharedPreferences
import com.github.pokatomnik.kriper.services.preferences.PreferencesBooleanValue

class SearchPreferences(private val sharedPreferences: SharedPreferences) {
    var searchValue: String = ""

    val fullTextSearch = FullTextSearchPreferences(object : PreferencesBooleanValue {
        private val FULL_TEXT_SEARCH = "FULL_TEXT_SEARCH"
        override fun write(value: Boolean) {
            sharedPreferences.edit().putBoolean(FULL_TEXT_SEARCH, value).apply()
        }

        override fun read(defaultValue: Boolean): Boolean {
            return sharedPreferences.getBoolean(FULL_TEXT_SEARCH, defaultValue)
        }
    })
}