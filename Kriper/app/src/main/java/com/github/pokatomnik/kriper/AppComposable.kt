package com.github.pokatomnik.kriper

import android.content.pm.ActivityInfo
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.github.pokatomnik.kriper.navigation.rememberNavigation
import com.github.pokatomnik.kriper.screens.search.Search
import com.github.pokatomnik.kriper.screens.allstories.AllStories
import com.github.pokatomnik.kriper.screens.alltags.AllTags
import com.github.pokatomnik.kriper.screens.favoritestories.FavoriteStories
import com.github.pokatomnik.kriper.screens.history.History
import com.github.pokatomnik.kriper.screens.home.Home
import com.github.pokatomnik.kriper.screens.settings.Settings
import com.github.pokatomnik.kriper.screens.storiesoftag.StoriesOfTag
import com.github.pokatomnik.kriper.screens.story.Story
import com.github.pokatomnik.kriper.screens.tag.TagsOfGroup
import com.github.pokatomnik.kriper.screens.taggroups.TagGroups
import com.github.pokatomnik.kriper.screens.video.Video
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LockScreenOrientation
import com.github.pokatomnik.kriper.ui.components.screen
import com.github.pokatomnik.kriper.ui.widgets.KriperBottomNavigation
import com.github.pokatomnik.kriper.ui.widgets.LocalScaffoldState
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppComposable() {
    IndexServiceReadiness { indexService ->
        val navigation = rememberNavigation()
        val scaffoldState = rememberScaffoldState()
        CompositionLocalProvider(LocalScaffoldState provides scaffoldState) {
            Scaffold(
                scaffoldState = scaffoldState,
                content = { scaffoldPaddingValues ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        AnimatedNavHost(
                            navController = navigation.navController,
                            startDestination = navigation.defaultRoute.route,
                            modifier = Modifier.padding(scaffoldPaddingValues)
                        ) {
                            screen(
                                route = navigation.homeRoute.route,
                            ) {
                                navigation.homeRoute.Params {
                                    Home(
                                        onNavigateToTagGroups = { navigation.tagGroupsRoute.navigate() },
                                        onNavigateToAllTags = { navigation.allTagsRoute.navigate() },
                                        onNavigateToAllStories = { navigation.allStoriesRoute.navigate() },
                                        onNavigateToHistory = { navigation.historyRoute.navigate() },
                                        onNavigateToFavoriteStories = { navigation.favoriteStoriesRoute.navigate() }
                                    )
                                }
                            }
                            screen(
                                route = navigation.tagGroupsRoute.route
                            ) {
                                navigation.tagGroupsRoute.Params {
                                    TagGroups(
                                        onNavigateToGroup = { navigation.tagsOfGroupRoute.navigate(it) },
                                        onNavigateBack = { navigation.navigateBack() }
                                    )
                                }
                            }
                            screen(
                                route = navigation.tagsOfGroupRoute.route,
                            ) {
                                navigation.tagsOfGroupRoute.Params { groupName ->
                                    TagsOfGroup(
                                        tagGroupTitle = groupName,
                                        navigateBack = { navigation.navigateBack() },
                                        navigateToStories = { tagName ->
                                            navigation.storiesOfTagOfGroupRoute
                                                .navigate(groupName, tagName)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.storiesOfTagOfGroupRoute.route
                            ) {
                                navigation.storiesOfTagOfGroupRoute.Params { groupName, tagName ->
                                    StoriesOfTag(
                                        tagGroupName = groupName,
                                        tagName = tagName,
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStory = { storyTitle ->
                                            navigation.storyRoute.navigate(storyTitle)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.storyRoute.route,
                                keepScreenOn = true
                            ) {
                                navigation.storyRoute.Params { storyTitle ->
                                    Story(
                                        storyTitle = storyTitle,
                                        onNavigateToTag = { tag ->
                                            navigation.storiesOfTagRoute.navigate(tag)
                                        },
                                        onNavigateToPrevious = { navigation.navigateBack() },
                                        onNavigateToRandom = {
                                            indexService.content
                                                .getRandomPageMeta()
                                                ?.also { navigation.storyRoute.navigate(it.title) }
                                                .let { it != null }
                                        },
                                        onNavigateToStory = { navigation.storyRoute.navigate(it) },
                                        onNavigateToVideo = { videoURL ->
                                            navigation.videoRoute.navigate(videoURL)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.settingsRoute.route,
                            ) {
                                navigation.settingsRoute.Params {
                                    Settings(
                                        onNavigateBack = { navigation.navigateBack() }
                                    )
                                }
                            }
                            screen(
                                route = navigation.allTagsRoute.route,
                            ) {
                                navigation.allTagsRoute.Params {
                                    AllTags(
                                        onNavigateBack = { navigation.navigateBack() },
                                        navigateToStories = { tagName ->
                                            navigation.storiesOfTagRoute.navigate(tagName)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.storiesOfTagRoute.route,
                            ) {
                                navigation.storiesOfTagRoute.Params { tagTitle ->
                                    StoriesOfTag(
                                        tagName = tagTitle,
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStory = { storyTitle ->
                                            navigation.storyRoute.navigate(storyTitle)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.allStoriesRoute.route
                            ) {
                                navigation.allStoriesRoute.Params {
                                    AllStories(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStory = { storyTitle ->
                                            navigation.storyRoute.navigate(storyTitle)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.historyRoute.route
                            ) {
                                navigation.historyRoute.Params {
                                    History(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStory = { storyTitle ->
                                            navigation.storyRoute.navigate(storyTitle)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.searchRoute.route
                            ) {
                                navigation.searchRoute.Params {
                                    Search(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStory = { storyTitle ->
                                            navigation.storyRoute.navigate(storyTitle)
                                        },
                                        onNavigateToTag = { tagTitle ->
                                            navigation.storiesOfTagRoute.navigate(tagTitle)
                                        },
                                        onNavigateToTagGroup = { tagGroupTitle ->
                                            navigation.tagsOfGroupRoute.navigate(tagGroupTitle)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.favoriteStoriesRoute.route
                            ) {
                                navigation.favoriteStoriesRoute.Params {
                                    FavoriteStories(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStory = { storyTitle ->
                                            navigation.storyRoute.navigate(storyTitle)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.videoRoute.route
                            ) {
                                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                                    navigation.videoRoute.Params { videoURL ->
                                        Video(videoURL = videoURL)
                                    }
                                }
                            }
                        }
                    }
                },
                bottomBar = { KriperBottomNavigation(navigation = navigation) }
            )
        }
    }
}