package com.github.pokatomnik.kriper.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.services.serializer.rememberSerializer
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberNavigation(): Navigation {
    val navHostController = rememberNavController()
    val serializer = rememberSerializer()

    return Navigation(
        navController = navHostController,
        serializer = serializer
    )
}