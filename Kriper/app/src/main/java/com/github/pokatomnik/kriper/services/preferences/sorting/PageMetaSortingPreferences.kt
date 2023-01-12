package com.github.pokatomnik.kriper.services.preferences.sorting

import android.content.SharedPreferences
import com.github.pokatomnik.kriper.services.preferences.PreferencesStringValue

class PageMetaSortingPreferences(private val sharedPreferences: SharedPreferences) {
    val pageMetaSorting = PageMetaSorting(object : PreferencesStringValue {
        private val PAGE_META_SORTING_KEY = "PAGE_META_SORTING_KEY"
        override fun write(value: String) {
            sharedPreferences.edit().putString(PAGE_META_SORTING_KEY, value).apply()
        }
        override fun read(defaultValue: String): String {
            return sharedPreferences.getString(
                PAGE_META_SORTING_KEY,
                defaultValue
            ) ?: defaultValue
        }
    })
}