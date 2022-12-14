@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
package com.github.pokatomnik.kriper.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.pokatomnik.kriper.services.serializer.Serializer
import com.github.pokatomnik.kriper.services.serializer.rememberSerializer
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

data class Navigation(
    val navController: NavHostController,
    private val serializer: Serializer
) {
    private fun NavHostController.navigateDistinct(route: String) {
        navigate(route) { launchSingleTop = true;  }
    }

    private fun NavHostController.navigateAllowSame(route: String) {
        navigate(route)
    }

    private fun NavDestination?.on(route: String): Boolean {
        return this?.hierarchy?.any { it.route == route } == true
    }

    fun navigateDistinct(route: String) {
        navController.navigateDistinct(route)
    }

    @Composable
    private fun rememberCurrentDestination(): NavDestination? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination
    }

    val tagGroupsRoute = object : RouteNoParameters {
        private val routePath = "/tag-groups"

        @Composable
        override fun on(): Boolean {
            val currentDestination = rememberCurrentDestination()
            return currentDestination.on(routePath)
        }

        override fun navigate() {
            navController.navigateDistinct(routePath)
        }

        @Composable
        override fun Params(content: @Composable () -> Unit) {
            content()
        }

        override val route: String
            get() = routePath
    }

    val defaultRoute: Route
        get() = tagGroupsRoute

    val tagsOfGroupRoute = object : RouteSingleParameter {
        private val TAG_GROUP_NAME_KEY = "TAG_GROUP_NAME_KEY"

        override fun navigate(tagGroupTitle: String) {
            val serializedTagGroupTitle = serializer.serialize(tagGroupTitle)
            navController.navigateDistinct("/tag-groups/$serializedTagGroupTitle")
        }

        @Composable
        override fun Params(content: @Composable (tagGroupName: String) -> Unit) {
            navController
                .currentBackStackEntryAsState()
                .value
                ?.arguments
                ?.getString(TAG_GROUP_NAME_KEY)
                ?.let(serializer::parse)
                .let { rememberLastNonNull(it) }
                ?.let { content(it) }
        }

        override val route: String
            get() = "/tag-groups/{$TAG_GROUP_NAME_KEY}"
    }

    val storiesOfTagRoute = object : RouteTwoParameters {
        private val TAG_GROUP_NAME_KEY = "TAG_GROUP_NAME_KEY"

        private val TAG_NAME_KEY = "TAG_NAME_KEY"

        override fun navigate(tagGroupName: String, tagName: String) {
            val serializedTagGroupTitle = serializer.serialize(tagGroupName)
            val serializedTagName = serializer.serialize(tagName)
            navController.navigateDistinct("/tag-groups/${serializedTagGroupTitle}/${serializedTagName}")
        }

        @Composable
        override fun Params(content: @Composable (tagGroupName: String, tagName: String) -> Unit) {
            val arguments = navController
                .currentBackStackEntryAsState()
                .value
                ?.arguments
            val tagGroupName = arguments?.getString(TAG_GROUP_NAME_KEY)?.let(serializer::parse)
                .let { rememberLastNonNull(it) }
            val tagName = arguments?.getString(TAG_NAME_KEY)?.let(serializer::parse)
                .let { rememberLastNonNull(it) }
            if (tagGroupName != null && tagName != null) content(tagGroupName, tagName)
        }

        override val route: String
            get() = "/tag-groups/{$TAG_GROUP_NAME_KEY}/{$TAG_NAME_KEY}"
    }

    val storyRoute = object : RouteSingleParameter {
        private val STORY_TITLE_KEY = "STORY_TITLE_KEY"

        override fun navigate(storyTitle: String) {
            val serializedStoryTitle = serializer.serialize(storyTitle)
            navController.navigate("/story/${serializedStoryTitle}")
        }

        @Composable
        override fun Params(
            content: @Composable (storyName: String) -> Unit
        ) {
            val arguments = navController.currentBackStackEntry?.arguments
            val storyName = arguments?.getString(STORY_TITLE_KEY)?.let(serializer::parse)
                .let { rememberLastNonNull(it) }
            if (storyName != null) content(
                storyName
            )
        }

        override val route: String
            get() = "/story/{$STORY_TITLE_KEY}"
    }

    val settingsRoute = object : RouteNoParameters {
        private val routePath = "/settings"

        @Composable
        override fun on(): Boolean {
            val currentDestination = rememberCurrentDestination()
            return currentDestination.on(routePath)
        }

        override fun navigate() {
            navController.navigateDistinct(routePath)
        }

        @Composable
        override fun Params(content: @Composable () -> Unit) {
            content()
        }

        override val route: String
            get() = routePath
    }
}

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