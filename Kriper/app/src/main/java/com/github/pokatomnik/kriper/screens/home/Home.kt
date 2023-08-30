package com.github.pokatomnik.kriper.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.pokatomnik.kriper.ui.components.PageContainer

@Composable
fun Home(
    onNavigateToTagGroups: () -> Unit,
    onNavigateToAllTags: () -> Unit,
    onNavigateToAllStories: () -> Unit,
    onNavigateToShortStories: () -> Unit,
    onNavigateToLongStories: () -> Unit,
    onNavigateToNewStories: () -> Unit,
    onNavigateToGoldStories: () -> Unit,
    onNavigateToWeekTop: () -> Unit,
    onNavigateToMonthTop: () -> Unit,
    onNavigateToYearTop: () -> Unit,
    onNavigateToAllTheTimeTop: () -> Unit,
    onNavigateToReadStories: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToFavoriteStories: () -> Unit,
    onNavigateToRandom: () -> Any,
) {
    PageContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HomeHorizontalSpacerSmall()
            HeaderBlock()
            HomeHorizontalSpacerSmall()
            SelectionsBlock(
                onNavigateToAllStories = onNavigateToAllStories,
                onNavigateToNewStories = onNavigateToNewStories,
                onNavigateToShortStories = onNavigateToShortStories,
                onNavigateToLongStories = onNavigateToLongStories,
                onNavigateToGoldStories = onNavigateToGoldStories,
            )
            HomeHorizontalSpacerLarge()
            TopBlock(
                onNavigateToWeekTop = onNavigateToWeekTop,
                onNavigateToMonthTop = onNavigateToMonthTop ,
                onNavigateToYearTop = onNavigateToYearTop,
                onNavigateToAllTheTimeTop = onNavigateToAllTheTimeTop,
            )
            HomeHorizontalSpacerLarge()
            StatisticsBlock()
            HomeHorizontalSpacerLarge()
            PersonalBlock(
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToFavoriteStories = onNavigateToFavoriteStories,
                onNavigateToReadStories = onNavigateToReadStories
            )
            HomeHorizontalSpacerLarge()
            HierarchyBlock(
                onNavigateToTagGroups = onNavigateToTagGroups,
                onNavigateToAllTags = onNavigateToAllTags,
                onNavigateToRandom = onNavigateToRandom
            )
            HomeHorizontalSpacerLarge()
        }
    }
}