package com.github.pokatomnik.kriper.services.preferences

import android.content.Context
import android.content.SharedPreferences
import com.github.pokatomnik.kriper.services.preferences.global.GlobalPreferences
import com.github.pokatomnik.kriper.services.preferences.page.PagePreferences
import com.github.pokatomnik.kriper.services.preferences.sorting.PageMetaSortingPreferences

class Preferences(private val context: Context) {
    private fun getPreferencesByName(name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    val pagePreferences = PagePreferences(
        getPreferencesByName("PAGE_PREFERENCES")
    )

    val globalPreferences = GlobalPreferences(
        getPreferencesByName("GLOBAL_PREFERENCES")
    )

    val sortingPreferences = PageMetaSortingPreferences(
        getPreferencesByName("SORTING_PREFERENCES")
    )
}