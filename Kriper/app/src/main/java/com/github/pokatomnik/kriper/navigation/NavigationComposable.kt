package com.github.pokatomnik.kriper.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.services.serializer.rememberSerializer
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberNavigation(): Navigation {
    val navHostController = rememberAnimatedNavController()
    val serializer = rememberSerializer()

    return Navigation(
        navController = navHostController,
        serializer = serializer
    )
}