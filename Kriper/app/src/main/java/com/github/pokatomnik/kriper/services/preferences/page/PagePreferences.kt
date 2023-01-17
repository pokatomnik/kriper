package com.github.pokatomnik.kriper.services.preferences.page

import android.content.SharedPreferences
import com.github.pokatomnik.kriper.services.preferences.PreferencesIntValue
import com.github.pokatomnik.kriper.services.preferences.PreferencesStringValue

class PagePreferences(private val sharedPreferences: SharedPreferences) {
    val fontSize = FontSize(object : PreferencesIntValue {
        private val FONT_SIZE_KEY = "FONT_SIZE_KEY"
        override fun write(value: Int) {
            sharedPreferences.edit().putInt(FONT_SIZE_KEY, value).apply()
        }
        override fun read(defaultValue: Int): Int {
            return sharedPreferences.getInt(FONT_SIZE_KEY, defaultValue)
        }
    })

    val storyContentFontFamily = FontFamily(object : PreferencesStringValue {
        private val STORY_CONTENT_FONT_FAMILY_KEY = "STORY_CONTENT_FONT_FAMILY_KEY"
        override fun write(value: String) {
            sharedPreferences
                .edit()
                .putString(STORY_CONTENT_FONT_FAMILY_KEY, value)
                .apply()
        }
        override fun read(defaultValue: String): String {
            return sharedPreferences
                .getString(STORY_CONTENT_FONT_FAMILY_KEY, defaultValue) ?: defaultValue
        }
    })
}