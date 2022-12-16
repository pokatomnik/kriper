package com.github.pokatomnik.kriper.services.preferences

import android.content.Context
import android.content.SharedPreferences
import com.github.pokatomnik.kriper.services.preferences.page.PagePreferences

class Preferences(private val context: Context) {
    private fun getPreferencesByName(name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    val pagePreferences = PagePreferences(
        getPreferencesByName("PAGE_PREFERENCES")
    )
}