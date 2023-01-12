package com.github.pokatomnik.kriper.services.preferences.sorting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.pokatomnik.kriper.services.preferences.PreferencesStringValue

class PageMetaSorting(private val preferencesValue: PreferencesStringValue) {
    private val defaultSorter: PageMetaSorter = AlphaASC()

    val availableSortersByID: Map<String, PageMetaSorter> = mutableMapOf<String, PageMetaSorter>()
        .apply {
            defaultSorter.apply { set(id, this) }
            AlphaDESC().apply { set(id, this) }

            DateASC().apply { set(id, this) }
            DateDESC().apply { set(id, this) }

            NumberOfViewsASC().apply { set(id, this) }
            NumberOfViewsDESC().apply { set(id, this) }

            ReadingTimeASC().apply { set(id, this) }
            ReadingTimeDESC().apply { set(id, this) }

            RatingASC().apply { set(id, this) }
            RatingDESC().apply { set(id, this) }
        }

    @Composable
    fun collectAsState(): MutableState<PageMetaSorter> {
        val (sortingID, setSortingID) = remember {
            val savedSortingID = preferencesValue.read(defaultSorter.id)
            mutableStateOf(savedSortingID)
        }
        val sorter = availableSortersByID[sortingID] ?: defaultSorter
        val setSorter: (PageMetaSorter) -> Unit = { setSortingID(it.id) }

        return object : MutableState<PageMetaSorter> {
            override var value: PageMetaSorter
                get() = sorter
                set(value) {
                    preferencesValue.write(value.id)
                    setSorter(value)
                }
            override fun component1(): PageMetaSorter {
                return sorter
            }
            override fun component2(): (PageMetaSorter) -> Unit {
                return {
                    preferencesValue.write(it.id)
                    setSorter(it)
                }
            }
        }
    }
}