package com.github.pokatomnik.kriper.screens.selections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.pokatomnik.kriper.ext.asMonthCyrillic

@Composable
fun StoriesByYearAndMonth(
    year: Int,
    month: Int,
    onNavigateBack: () -> Unit,
    onNavigateToStoryById: (storyId: String) -> Unit,
) {
    val (selectionComposable) = remember {
        mutableStateOf(
            makeParametrizedSelection(
                selectionTitle = "${month.asMonthCyrillic()}, ${year}г.",
                getSelection = { selections ->
                    selections.yearToMonths[year]?.get(month) ?: listOf()
                }
            )
        )
    }

    selectionComposable(
        onNavigateBack = onNavigateBack,
        onNavigateToStoryById = onNavigateToStoryById
    )
}