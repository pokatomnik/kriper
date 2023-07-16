package com.github.pokatomnik.kriper.services.preferences.global

import android.content.SharedPreferences
import com.github.pokatomnik.kriper.services.preferences.PreferencesBooleanValue
import com.github.pokatomnik.kriper.services.preferences.PreferencesStringValue

class GlobalPreferences(private val sharedPreferences: SharedPreferences) {
    val themeSelection = Theme(
        object : PreferencesStringValue {
            private val THEME_IDENTIFIER_KEY = "THEME_IDENTIFIER_KEY"
            override fun write(value: String) {
                sharedPreferences.edit().putString(THEME_IDENTIFIER_KEY, value).apply()
            }

            override fun read(defaultValue: String): String {
                return sharedPreferences
                    .getString(
                        THEME_IDENTIFIER_KEY,
                        defaultValue
                    ) ?: defaultValue
            }
        }
    )

    val oneTimeRunners = OneTimeRunners(object : PreferencesStringValue {
        private val ONE_TIME_RUNNERS_KEY = "ONE_TIME_RUNNERS_KEY"
        override fun write(value: String) {
            sharedPreferences.edit().putString(ONE_TIME_RUNNERS_KEY, value).apply()
        }
        override fun read(defaultValue: String): String {
            return sharedPreferences.getString(ONE_TIME_RUNNERS_KEY, defaultValue) ?: defaultValue
        }
    })

    val hideReadStories = HideReadStories(object : PreferencesBooleanValue {
        private val HIDE_READ_STORIES = "HIDE_READ_STORIES"
        override fun write(value: Boolean) {
            sharedPreferences.edit().putBoolean(HIDE_READ_STORIES, value).apply()
        }

        override fun read(defaultValue: Boolean): Boolean {
            return sharedPreferences.getBoolean(HIDE_READ_STORIES, defaultValue)
        }
    })
}