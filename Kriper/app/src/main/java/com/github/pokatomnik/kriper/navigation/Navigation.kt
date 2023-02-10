@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
package com.github.pokatomnik.kriper.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.pokatomnik.kriper.services.serializer.Serializer

data class Navigation(
    val navController: NavHostController,
    private val serializer: Serializer
) {
    private fun NavHostController.navigateDistinct(route: String) {
        navigate(route) { launchSingleTop = true; }
    }

    private fun NavHostController.navigateAllowSame(route: String) {
        navigate(route)
    }

    private fun NavDestination?.on(route: String): Boolean {
        return this?.hierarchy?.any { it.route == route } == true
    }

    @Composable
    private fun rememberCurrentDestination(): NavDestination? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination
    }

    /**
     * This must point to a home route. Must return any route listed below.
     */
    val defaultRoute: Route
        get() = homeRoute

    fun navigateBack(): Boolean {
        return navController.popBackStack()
    }

    val homeRoute = object : RouteNoParameters {
        private val routePath = "/home"
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
    /**
     * This is the top level of tags hierarchy.
     * All tags presented are grouped by tag groups.
     * This route must display all tag groups.
     * This is unparametrized route
     */
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

    /**
     * This route must list all tags in the selected tags group.
     */
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

    /**
     * This route must display all stories of selected tag of selected tag group.
     */
    val storiesOfTagOfGroupRoute = object : RouteTwoParameters {
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

    /**
     * Story route.
     * Must display selected story
     */
    val storyRoute = object : RouteSingleParameter {
        private val STORY_TITLE_KEY = "STORY_TITLE_KEY"

        override fun navigate(storyTitle: String) {
            val serializedStoryTitle = serializer.serialize(storyTitle)
            navController.navigateAllowSame("/story/${serializedStoryTitle}")
        }

        @Composable
        override fun Params(
            content: @Composable (storyName: String) -> Unit
        ) {
            val arguments = navController.currentBackStackEntryAsState().value?.arguments
            val storyName = arguments?.getString(STORY_TITLE_KEY)?.let(serializer::parse)
                .let { rememberLastNonNull(it) }
            if (storyName != null) content(
                storyName
            )
        }

        override val route: String
            get() = "/story/{$STORY_TITLE_KEY}"
    }

    val storyGalleryRoute = object : RouteSingleParameter {
        private val STORY_TITLE_KEY = "STORY_TITLE_KEY"

        override fun navigate(storyTitle: String) {
            val serializedStoryTitle = serializer.serialize(storyTitle)
            navController.navigate("/story/${serializedStoryTitle}/images")
        }

        @Composable
        override fun Params(
            content: @Composable (storyName: String) -> Unit,
        ) {
            val arguments = navController.currentBackStackEntryAsState().value?.arguments
            val storyName = arguments?.getString(STORY_TITLE_KEY)?.let(serializer::parse)
                ?.let { rememberLastNonNull(it) }
            if (storyName != null) content(
                storyName
            )
        }

        override val route: String
            get() = "/story/{${STORY_TITLE_KEY}}/images"
    }

    val storyGalleryImageRoute = object : RouteTwoParameters {
        private val STORY_TITLE_KEY = "STORY_TITLE_KEY"

        private val STORY_IMAGE_INDEX_KEY = "STORY_IMAGE_INDEX_KEY"

        override fun navigate(storyTitle: String, imageIndexAsString: String) {
            val serializedStoryTitle = serializer.serialize(storyTitle)
            val serializedStoryImageIndex = serializer.serialize(imageIndexAsString)
            navController.navigate("/story/${serializedStoryTitle}/images/${serializedStoryImageIndex}")
        }

        @Composable
        override fun Params(
            content: @Composable (storyName: String, imageIndex: String) -> Unit
        ) {
            val arguments = navController.currentBackStackEntryAsState().value?.arguments
            val storyName = arguments?.getString(STORY_TITLE_KEY)
                ?.let(serializer::parse)
                ?.let { rememberLastNonNull(it) }
            val imageIndex = arguments?.getString(STORY_IMAGE_INDEX_KEY)
                ?.let(serializer::parse)
                ?.let { rememberLastNonNull(it) }
            if (storyName != null && imageIndex != null) {
                content(storyName, imageIndex)
            }
        }

        override val route: String
            get() = "/story/{${STORY_TITLE_KEY}}/images/{${STORY_IMAGE_INDEX_KEY}}"
    }

    /**
     * This route must display all tags presented in the content.
     */
    val allTagsRoute = object : RouteNoParameters {
        private val routePath = "/all-tags"

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

    val storiesOfTagRoute = object : RouteSingleParameter {
        private val TAG_NAME_KEY = "TAG_NAME_KEY"

        override fun navigate(tagName: String) {
            val serializedTagName = serializer.serialize(tagName)
            navController.navigateDistinct("/tag/${serializedTagName}")
        }

        @Composable
        override fun Params(content: @Composable (tagName: String) -> Unit) {
            val arguments = navController
                .currentBackStackEntryAsState()
                .value
                ?.arguments
            val tagName = arguments
                ?.getString(TAG_NAME_KEY)
                ?.let(serializer::parse)
                .let { rememberLastNonNull(it) }
            tagName?.let { content(it) }
        }

        override val route: String
            get() = "/tag/{${TAG_NAME_KEY}}"
    }

    val videoRoute = object : RouteSingleParameter {
        private val VIDEO_URL_KEY = "VIDEO_URL_KEY"

        override fun navigate(videoURL: String) {
            val serializedVideoURL = serializer.serialize(videoURL)
            navController.navigateDistinct("/video/$serializedVideoURL")
        }

        @Composable
        override fun Params(content: @Composable (videoURL: String) -> Unit) {
            val arguments = navController
                .currentBackStackEntryAsState()
                .value
                ?.arguments
            val videoURL = arguments
                ?.getString(VIDEO_URL_KEY)
                ?.let(serializer::parse)
                .let { rememberLastNonNull(it) }
            videoURL?.let { content(it) }
        }

        override val route: String
            get() = "/video/{${VIDEO_URL_KEY}}"
    }

    /**
     * This route must display all stories presented in the content
     */
    val allStoriesRoute = object : RouteNoParameters {
        private val routePath = "/selections/all-stories"

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

    val allStoriesRouteByAuthor = object : RouteSingleParameter {
        private val AUTHOR_KEY = "AUTHOR_KEY"

        override fun navigate(authorRealName: String) {
            val serializedAuthorRealName = serializer.serialize(authorRealName)
            navController.navigateDistinct("/selections/all-stories/author/$serializedAuthorRealName")
        }

        @Composable
        override fun Params(content: @Composable (authorRealName: String) -> Unit) {
            val arguments = navController
                .currentBackStackEntryAsState()
                .value
                ?.arguments
            val authorRealName = arguments
                ?.getString(AUTHOR_KEY)
                ?.let(serializer::parse)
                .let { rememberLastNonNull(it) }
            authorRealName?.let { content(it) }
        }

        override val route: String
            get() = "/selections/all-stories/author/{${AUTHOR_KEY}}"
    }

    val shortMostVotedStoriesRoute = object : RouteNoParameters {
        private val routePath = "/selections/short"

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

    val longMostVotedStoriesRoute = object : RouteNoParameters {
        private val routePath = "/selections/long"

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

    val newStoriesRoute = object : RouteNoParameters {
        private val routePath = "/selections/new"

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

    /**
     * Route for displaying application settings
     */
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

    val historyRoute = object : RouteNoParameters {
        private val routePath = "/history"

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

    val searchRoute = object : RouteNoParameters {
        private val routePath = "/search"

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

    val favoriteStoriesRoute = object : RouteNoParameters {
        private val routePath = "/favorite/stories"

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

