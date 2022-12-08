package com.github.pokatomnik.kriper

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.github.pokatomnik.kriper.navigation.rememberNavigation
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
                            route = navigation.tagGroupsRoute.route,
                            main = true,
                        ) {
                            navigation.tagGroupsRoute.Params {
                                TagGroups(
                                    onNavigateToGroup = {
                                        navigation.tagsOfGroupRoute.navigate(it)
                                    }
                                )
                            }
                        }
                        screen(
                            route = navigation.tagsOfGroupRoute.route,
                            main = false,
                        ) {
                            navigation.tagsOfGroupRoute.Params { groupName ->
                                Tag(
                                    tagTitle = groupName,
                                    onNavigateToTagGroups = {
                                        navigation.tagGroupsRoute.navigate()
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
                        selected = navigation.tagGroupsRoute.on(),
                        onClick = { navigation.tagGroupsRoute.navigate() },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.GroupWork,
                                contentDescription = "Группы тегов"
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