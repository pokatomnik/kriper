package com.github.pokatomnik.kriper

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.navigation.NavigationProvider
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
        NavigationProvider {
            val navigation = rememberNavigation()
            AnimatedNavHost(
                navController = navigation.navController,
                startDestination = navigation.tagGroupsRoute.route
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
    }
}