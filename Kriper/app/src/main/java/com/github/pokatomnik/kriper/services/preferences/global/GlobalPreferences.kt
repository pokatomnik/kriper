package com.github.pokatomnik.kriper.services.preferences.global

import android.content.SharedPreferences
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
}