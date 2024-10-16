package com.github.pokatomnik.kriper

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.github.pokatomnik.kriper.ext.Subscriber
import com.github.pokatomnik.kriper.navigation.rememberNavigation
import com.github.pokatomnik.kriper.screens.addbookmark.AddBookmark
import com.github.pokatomnik.kriper.screens.allstoriesbyauthor.AllStoriesByAuthor
import com.github.pokatomnik.kriper.screens.alltags.AllTags
import com.github.pokatomnik.kriper.screens.favoritestories.FavoriteStories
import com.github.pokatomnik.kriper.screens.gallery.Gallery
import com.github.pokatomnik.kriper.screens.galleryimage.GalleryImage
import com.github.pokatomnik.kriper.screens.history.History
import com.github.pokatomnik.kriper.screens.home.Home
import com.github.pokatomnik.kriper.screens.listbookmarks.ListBookmarks
import com.github.pokatomnik.kriper.screens.months.Months
import com.github.pokatomnik.kriper.screens.search.Search
import com.github.pokatomnik.kriper.screens.selections.*
import com.github.pokatomnik.kriper.screens.settings.Settings
import com.github.pokatomnik.kriper.screens.storiesoftag.StoriesOfTag
import com.github.pokatomnik.kriper.screens.story.Story
import com.github.pokatomnik.kriper.screens.tag.TagsOfGroup
import com.github.pokatomnik.kriper.screens.taggroups.TagGroups
import com.github.pokatomnik.kriper.screens.video.Video
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.screen
import com.github.pokatomnik.kriper.ui.widgets.KriperBottomNavigation
import com.github.pokatomnik.kriper.ui.widgets.LocalScaffoldState
import androidx.navigation.compose.NavHost
import com.github.pokatomnik.kriper.screens.about.About
import com.github.pokatomnik.kriper.screens.aipowers.AIPowers
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppComposable(
    deeplinkHolder: DeeplinkHolder,
    deeplinkSubscriber: Subscriber<Uri?>
) {
    IndexServiceReadiness { indexService ->
        val scope = rememberCoroutineScope()
        val navigation = rememberNavigation()
        val scaffoldState = rememberScaffoldState()

        val tryOpenStory: (uri: Uri) -> Unit = { uri ->
            val storyId = uri.getStoryId()
            if (storyId == null) scope.launch {
                indexService.content.findStoryByPathMatch(uri)?.let { pageMeta ->
                    navigation.storyRoute.navigate(pageMeta.storyId)
                }
            } else {
                navigation.storyRoute.navigate(storyId)
            }
        }

        deeplinkHolder.Once { uri ->
            tryOpenStory(uri)
        }

        DisposableEffect(deeplinkSubscriber) {
            val subscription = deeplinkSubscriber.subscribe { uri ->
                uri?.let { tryOpenStory(uri) }
            }
            onDispose { subscription.unsubscribe() }
        }

        CompositionLocalProvider(LocalScaffoldState provides scaffoldState) {
            Scaffold(
                scaffoldState = scaffoldState,
                content = { scaffoldPaddingValues ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        NavHost(
                            navController = navigation.navController,
                            startDestination = navigation.defaultRoute.route,
                            modifier = Modifier.padding(scaffoldPaddingValues)
                        ) {
                            val onNavigateToRandom = {
                                indexService.content
                                    .getRandomPageMeta()
                                    ?.also { navigation.storyRoute.navigate(it.storyId) }
                                    .let { it != null }
                            }

                            screen(
                                route = navigation.homeRoute.route,
                            ) {
                                navigation.homeRoute.Params {
                                    Home(
                                        onNavigateToTagGroups = { navigation.tagGroupsRoute.navigate() },
                                        onNavigateToAllTags = { navigation.allTagsRoute.navigate() },
                                        onNavigateToAllStories = { navigation.allStoriesRoute.navigate() },
                                        onNavigateToShortStories = { navigation.shortMostVotedStoriesRoute.navigate() },
                                        onNavigateToLongStories = { navigation.longMostVotedStoriesRoute.navigate() },
                                        onNavigateToGoldStories = { navigation.goldStoriesRoute.navigate() },
                                        onNavigateToWeekTop = { navigation.weekTopRoute.navigate() },
                                        onNavigateToMonthTop = { navigation.monthTopRoute.navigate() },
                                        onNavigateToYearTop = { navigation.yearTopRoute.navigate() },
                                        onNavigateToAllTheTimeTop = { navigation.allTheTimeTopRoute.navigate() },
                                        onNavigateToNewStories = { navigation.newStoriesRoute.navigate() },
                                        onNavigateToReadStories = { navigation.readStoriesRoute.navigate() },
                                        onNavigateToHistory = { navigation.historyRoute.navigate() },
                                        onNavigateToFavoriteStories = { navigation.favoriteStoriesRoute.navigate() },
                                        onNavigateToRandom = onNavigateToRandom,
                                        onNavigateToBookmarks = { navigation.listAllBookmarksRoute.navigate() },
                                        onNavigateToYear = { year -> navigation.yearRoute.navigate(year.toString()) }
                                    )
                                }
                            }
                            screen(
                                route = navigation.tagGroupsRoute.route
                            ) {
                                navigation.tagGroupsRoute.Params {
                                    TagGroups(
                                        onNavigateToGroup = { navigation.tagsOfGroupRoute.navigate(it) },
                                        onNavigateToStoriesOfTagOfTagGroup = { groupTitle, tagTitle ->
                                            navigation.storiesOfTagOfGroupRoute.navigate(groupTitle, tagTitle)
                                        },
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
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.storyRoute.route,
                                keepScreenOn = true
                            ) {
                                navigation.storyRoute.Params { storyId ->
                                    Story(
                                        storyId = storyId,
                                        bookmarkScrollPosition = null,
                                        onNavigateToTag = { tag ->
                                            navigation.storiesOfTagRoute.navigate(tag)
                                        },
                                        onNavigateToPrevious = { navigation.navigateBack() },
                                        onNavigateToRandom = onNavigateToRandom,
                                        onNavigateToSearch = { navigation.searchRoute.navigate() },
                                        onNavigateToGallery = {
                                            navigation.storyGalleryRoute.navigate(storyId)
                                        },
                                        onNavigateToVideo = { videoURL ->
                                            navigation.videoRoute.navigate(videoURL)
                                        },
                                        onNavigateToAuthor = { authorRealName ->
                                            navigation.allStoriesRouteByAuthor.navigate(authorRealName)
                                        },
                                        onNavigateToAddBookmark = { scrollPosition ->
                                            navigation.addBookmarkRoute.navigate(
                                                storyId,
                                                scrollPosition.toString()
                                            )
                                        },
                                        onNavigateToAllBookmarksOfStory = {
                                            navigation.listStoryBookmarksRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.storyWithScrollRoute.route,
                                keepScreenOn = true
                            ) {
                                navigation.storyWithScrollRoute.Params { storyId, scrollPosition ->
                                    Story(
                                        storyId = storyId,
                                        bookmarkScrollPosition = scrollPosition.toInt(),
                                        onNavigateToTag = { tag ->
                                            navigation.storiesOfTagRoute.navigate(tag)
                                        },
                                        onNavigateToPrevious = { navigation.navigateBack() },
                                        onNavigateToRandom = onNavigateToRandom,
                                        onNavigateToSearch = { navigation.searchRoute.navigate() },
                                        onNavigateToGallery = {
                                            navigation.storyGalleryRoute.navigate(storyId)
                                        },
                                        onNavigateToVideo = { videoURL ->
                                            navigation.videoRoute.navigate(videoURL)
                                        },
                                        onNavigateToAuthor = { authorRealName ->
                                            navigation.allStoriesRouteByAuthor.navigate(authorRealName)
                                        },
                                        onNavigateToAddBookmark = { newScrollPosition ->
                                            navigation.addBookmarkRoute.navigate(
                                                storyId,
                                                newScrollPosition.toString()
                                            )
                                        },
                                        onNavigateToAllBookmarksOfStory = {
                                            navigation.listStoryBookmarksRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.storyGalleryRoute.route,
                            ) {
                                navigation.storyGalleryRoute.Params { storyId ->
                                    Gallery(
                                        storyId = storyId,
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToImage = { index ->
                                            navigation.storyGalleryImageRoute.navigate(
                                                storyId,
                                                index.toString()
                                            )
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.storyGalleryImageRoute.route,
                            ) {
                                navigation.storyGalleryImageRoute.Params { storyId, storyIndexAsString ->
                                    val imageIndex = storyIndexAsString.toInt()
                                    GalleryImage(
                                        storyId = storyId,
                                        imageIndex = imageIndex,
                                        onNavigateBack = { navigation.navigateBack() }
                                    )
                                }
                            }
                            screen(
                                route = navigation.settingsRoute.route,
                            ) {
                                navigation.settingsRoute.Params {
                                    Settings(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToAbout = { navigation.settingsAboutRoute.navigate() },
                                        onNavigateToAIPowers = { navigation.settingsAIPowersRoute.navigate() }
                                    )
                                }
                            }
                            screen (
                                route = navigation.settingsAboutRoute.route,
                            ) {
                                navigation.settingsAboutRoute.Params {
                                    About(onNavigateBack = { navigation.navigateBack() })
                                }
                            }
                            screen(
                                route = navigation.settingsAIPowersRoute.route
                            ) {
                                navigation.settingsAIPowersRoute.Params {
                                    AIPowers(onNavigateBack = { navigation.navigateBack() })
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
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
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
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.allStoriesRouteByAuthor.route
                            ) {
                                navigation.allStoriesRouteByAuthor.Params { authorRealName ->
                                    AllStoriesByAuthor(
                                        authorRealName = authorRealName,
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.shortMostVotedStoriesRoute.route
                            ) {
                                navigation.shortMostVotedStoriesRoute.Params {
                                    ShortStories(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.longMostVotedStoriesRoute.route
                            ) {
                                navigation.longMostVotedStoriesRoute.Params {
                                    LongStories(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.newStoriesRoute.route
                            ) {
                                navigation.newStoriesRoute.Params {
                                    NewStories(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.goldStoriesRoute.route
                            ) {
                                navigation.goldStoriesRoute.Params {
                                    GoldStories(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.weekTopRoute.route
                            ) {
                                navigation.weekTopRoute.Params {
                                    WeekTop(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.monthTopRoute.route
                            ) {
                                navigation.monthTopRoute.Params {
                                    MonthTop(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.yearTopRoute.route
                            ) {
                                navigation.yearTopRoute.Params {
                                    YearTop(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.allTheTimeTopRoute.route
                            ) {
                                navigation.allTheTimeTopRoute.Params {
                                    AllTheTimeTop(
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.readStoriesRoute.route
                            ) {
                                navigation.readStoriesRoute.Params {
                                    ReadStories(
                                        selectionTitle = "Прочитанные",
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
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
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
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
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
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
                                        onNavigateToStoryById = { storyId ->
                                            navigation.storyRoute.navigate(storyId)
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.videoRoute.route
                            ) {
                                navigation.videoRoute.Params { videoURL ->
                                    Video(videoURL = videoURL)
                                }
                            }
                            screen(
                                route = navigation.addBookmarkRoute.route,
                            ) {
                                navigation.addBookmarkRoute.Params { storyId, scrollPosition ->
                                    AddBookmark(
                                        storyId = storyId,
                                        scrollPosition = scrollPosition.toInt(),
                                        onNavigateBack = { navigation.navigateBack() }
                                    )
                                }
                            }
                            screen(
                                route = navigation.listStoryBookmarksRoute.route,
                            ) {
                                navigation.listStoryBookmarksRoute.Params { storyId ->
                                    ListBookmarks(
                                        storyId = storyId,
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryWithScrollPosition = { newStoryId, scrollPosition ->
                                            navigation.storyWithScrollRoute.navigate(
                                                newStoryId,
                                                scrollPosition.toString()
                                            )
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.listAllBookmarksRoute.route,
                            ) {
                                navigation.listAllBookmarksRoute.Params {
                                    ListBookmarks(
                                        storyId = null,
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryWithScrollPosition = { storyId, scrollPosition ->
                                            navigation.storyWithScrollRoute.navigate(
                                                storyId,
                                                scrollPosition.toString()
                                            )
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.yearRoute.route
                            ) {
                                navigation.yearRoute.Params { yearString ->
                                    Months(
                                        year = yearString.toInt(),
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoriesOfMonth = { month ->
                                            navigation.yearAndMonthRoute.navigate(yearString, month.toString())
                                        }
                                    )
                                }
                            }
                            screen(
                                route = navigation.yearAndMonthRoute.route
                            ) {
                                navigation.yearAndMonthRoute.Params { yearString, monthString ->
                                    StoriesByYearAndMonth(
                                        year = yearString.toInt(),
                                        month = monthString.toInt(),
                                        onNavigateBack = { navigation.navigateBack() },
                                        onNavigateToStoryById = { navigation.storyRoute.navigate(it) }
                                    )
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

data class DeeplinkHolder(val initialDeeplink: Uri?) {
    private var called = false
    @Composable
    fun Once(fn: (deeplink: Uri) -> Unit) {
        if (called || initialDeeplink == null) return
        LaunchedEffect(Unit) {
            fn(initialDeeplink)
            called = true
        }
    }
}

private fun Uri.getStoryId(): String? {
    return this.getQueryParameter("newsid")
}