package com.github.pokatomnik.kriper.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.services.preferences.sorting.PageMetaSorter
import com.github.pokatomnik.kriper.ui.components.SelectableRowRadio

@Composable
fun sortingStateWithUI(
    onMenuPress: (sorter: PageMetaSorter) -> Unit = {},
): Pair<MutableState<PageMetaSorter>, @Composable () -> Unit>{
    val pageMetaSorting = rememberPreferences()
        .sortingPreferences
        .pageMetaSorting
    val sorterState = pageMetaSorting.collectAsState()
    val (sorter, setSorter) = sorterState

    val renderOptions = @Composable {
        pageMetaSorting.availableSortersByID.forEach { (_, currentSorter) ->
            SelectableRowRadio(
                selected = sorter.id == currentSorter.id,
                onClick = {
                    setSorter(currentSorter)
                    onMenuPress(currentSorter)
                }
            ) {
                Text(currentSorter.title)
            }
        }
    }

    return Pair(sorterState, renderOptions)
}