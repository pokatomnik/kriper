package com.github.pokatomnik.kriper

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.navigation.rememberNavigation
import com.github.pokatomnik.kriper.screens.allstories.AllStories
import com.github.pokatomnik.kriper.screens.alltags.AllTags
import com.github.pokatomnik.kriper.screens.home.Home
import com.github.pokatomnik.kriper.screens.settings.Settings
import com.github.pokatomnik.kriper.screens.storiesoftag.StoriesOfTag
import com.github.pokatomnik.kriper.screens.story.Story
import com.github.pokatomnik.kriper.screens.tag.Tag
import com.github.pokatomnik.kriper.screens.taggroups.TagGroups
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.screen
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppComposable() {
    IndexServiceReadiness {
        val navigation = rememberNavigation()
        Scaffold(
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
                                    onNavigateToTagGroups = {
                                        navigation.tagGroupsRoute.navigate()
                                    },
                                    onNavigateToAllTags = {
                                        navigation.allTagsRoute.navigate()
                                    },
                                    onNavigateToAllStories = {
                                        navigation.allStoriesRoute.navigate()
                                    }
                                )
                            }
                        }
                        screen(
                            route = navigation.tagGroupsRoute.route
                        ) {
                            navigation.tagGroupsRoute.Params {
                                TagGroups(
                                    onNavigateToGroup = {
                                        navigation.tagsOfGroupRoute.navigate(it)
                                    },
                                    onNavigateBack = {
                                        navigation.homeRoute.navigate()
                                    }
                                )
                            }
                        }
                        screen(
                            route = navigation.tagsOfGroupRoute.route,
                        ) {
                            navigation.tagsOfGroupRoute.Params { groupName ->
                                Tag(
                                    tagGroupTitle = groupName,
                                    navigateBack = { navigation.tagGroupsRoute.navigate() },
                                    navigateToStories = { tagName ->
                                        navigation.storiesOfTagOfGroupRoute.navigate(groupName, tagName)
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
                                    onNavigateBack = {
                                        navigation.tagsOfGroupRoute.navigate(groupName)
                                    },
                                    onNavigateToStory = { storyTitle ->
                                        navigation.storyRoute.navigate(storyTitle)
                                    }
                                )
                            }
                        }
                        screen(
                            route = navigation.storyRoute.route
                        ) {
                            navigation.storyRoute.Params { storyTitle ->
                                Story(storyTitle = storyTitle)
                            }
                        }
                        screen(
                            route = navigation.settingsRoute.route,
                        ) {
                            navigation.settingsRoute.Params {
                                Settings(
                                    onNavigateBack = {
                                        navigation.homeRoute.navigate()
                                    }
                                )
                            }
                        }
                        screen(
                            route = navigation.allTagsRoute.route,
                        ) {
                            navigation.allTagsRoute.Params {
                                AllTags(
                                    onNavigateBack = {
                                        navigation.homeRoute.navigate()
                                    },
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
                                    onNavigateBack = {
                                        navigation.homeRoute.navigate()
                                    },
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
                                    onNavigateBack = {
                                        navigation.homeRoute.navigate()
                                    },
                                    onNavigateToStory = { storyTitle ->
                                        navigation.storyRoute.navigate(storyTitle)
                                    }
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = {
                BottomNavigation(
                    elevation = 1.dp
                ) {
                    BottomNavigationItem(
                        selected = navigation.homeRoute.on(),
                        onClick = { navigation.homeRoute.navigate() },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Главная"
                            )
                        }
                    )
                    BottomNavigationItem(
                        selected = navigation.settingsRoute.on(),
                        onClick = { navigation.settingsRoute.navigate() },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Настройки"
                            )
                        }
                    )
                }
            }
        )
    }
}