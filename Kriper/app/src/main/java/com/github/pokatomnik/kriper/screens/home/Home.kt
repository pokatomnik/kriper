package com.github.pokatomnik.kriper.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.*
import kotlinx.coroutines.launch
import com.github.pokatomnik.kriper.R
import com.github.pokatomnik.kriper.ui.theme.isLocalAppDarkThemeEnabled

@Composable
private fun HomeHorizontalSpacerSmall() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(SMALL_PADDING.dp)
    )
}

@Composable
private fun HomeHorizontalSpacerLarge() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(LARGE_PADDING.dp)
    )
}

@Composable
fun Home(
    onNavigateToTagGroups: () -> Unit,
    onNavigateToAllTags: () -> Unit,
    onNavigateToAllStories: () -> Unit,
    onNavigateToShortStories: () -> Unit,
    onNavigateToLongStories: () -> Unit,
    onNavigateToNewStories: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToFavoriteStories: () -> Unit,
    onNavigateToRandom: () -> Any,
) {
    val animatedTitleTranslateX = remember { Animatable(-100f) }
    val animatedTitleAlpha = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        launch {
            animatedTitleTranslateX.animateTo(
                targetValue = 0f,
                animationSpec = tween(2000)
            )
        }
        launch {
            animatedTitleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(2000)
            )
        }
    }

    PageContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HomeHorizontalSpacerSmall()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LARGE_PADDING.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "...Kriper",
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.graphicsLayer {
                        translationX = animatedTitleTranslateX.value
                        alpha = animatedTitleAlpha.value
                    }
                )
            }
            HomeHorizontalSpacerSmall()
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMALL_PADDING.dp)
                ) {
                    Spacer(
                        modifier = Modifier.width(LARGE_PADDING.dp)
                    )
                    Text(
                        text = "ГЛАВНОЕ",
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(
                    modifier = Modifier.height(SMALL_PADDING.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMALL_PADDING.dp)
                ) {
                    IconicCardFull(
                        title = "Группы меток",
                        icon = Icons.Filled.Category,
                        description = "Проще выбрать что почитать",
                        backgroundTile = ImageBitmap.imageResource(
                            if (isLocalAppDarkThemeEnabled()) {
                                R.drawable.pattern_tag_group_dark
                            } else {
                                R.drawable.pattern_tag_group_light
                            }
                        ),
                        onClick = onNavigateToTagGroups
                    )
                }
                HomeHorizontalSpacerSmall()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMALL_PADDING.dp)
                ) {
                    IconicCardFull(
                        title = "Все метки",
                        icon = Icons.Filled.Tag,
                        description = "Если удобнее по алфавиту",
                        backgroundTile = ImageBitmap.imageResource(
                            if (isLocalAppDarkThemeEnabled()) {
                                R.drawable.pattern_tag_dark
                            } else {
                                R.drawable.pattern_tag_light
                            }
                        ),
                        onClick = onNavigateToAllTags,
                    )
                }
                HomeHorizontalSpacerSmall()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMALL_PADDING.dp)
                ) {
                    IconicCardFull(
                        title = "Случайная",
                        icon = Icons.Outlined.Casino,
                        description = "Просто откройте мне историю",
                        backgroundTile = ImageBitmap.imageResource(
                            if (isLocalAppDarkThemeEnabled()) {
                                R.drawable.pattern_random_dark
                            } else {
                                R.drawable.pattern_random_light
                            }
                        ),
                        onClick = { onNavigateToRandom() }
                    )
                }
            }
            HomeHorizontalSpacerLarge()
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMALL_PADDING.dp)
                ) {
                    Spacer(
                        modifier = Modifier.width(LARGE_PADDING.dp)
                    )
                    Text(
                        text = "ПОДБОРКИ",
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(
                    modifier = Modifier.height(SMALL_PADDING.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    Spacer(
                        modifier = Modifier.width(SMALL_PADDING.dp)
                    )
                    IconicCardSmall(
                        icon = Icons.Filled.HistoryEdu,
                        title = "Все",
                        backgroundTile = ImageBitmap.imageResource(
                            if (isLocalAppDarkThemeEnabled()) {
                                R.drawable.pattern_all_stories_dark
                            } else {
                                R.drawable.pattern_all_stories_light
                            }
                        ),
                        onClick = onNavigateToAllStories
                    )
                    Spacer(
                        modifier = Modifier.width(SMALL_PADDING.dp)
                    )
                    IconicCardSmall(
                        icon = Icons.Filled.ReceiptLong,
                        title = "Короткие",
                        backgroundTile = ImageBitmap.imageResource(
                            if (isLocalAppDarkThemeEnabled()) {
                                R.drawable.pattern_short_stories_dark
                            } else {
                                R.drawable.pattern_short_stories_light
                            }
                        ),
                        onClick = onNavigateToShortStories,
                    )
                    Spacer(
                        modifier = Modifier.width(SMALL_PADDING.dp)
                    )
                    IconicCardSmall(
                        icon = Icons.Filled.AutoStories,
                        title = "Длинные",
                        backgroundTile = ImageBitmap.imageResource(
                            if (isLocalAppDarkThemeEnabled()) {
                                R.drawable.pattern_long_stories_dark
                            } else {
                                R.drawable.pattern_long_stories_light
                            }
                        ),
                        onClick = onNavigateToLongStories,
                    )
                    Spacer(
                        modifier = Modifier.width(SMALL_PADDING.dp)
                    )
                    IconicCardSmall(
                        icon = Icons.Filled.FiberNew,
                        title = "Новые",
                        backgroundTile = ImageBitmap.imageResource(
                            if (isLocalAppDarkThemeEnabled()) {
                                R.drawable.pattern_new_stories_dark
                            } else {
                                R.drawable.pattern_new_stories_light
                            }
                        ),
                        onClick = onNavigateToNewStories,
                    )
                    Spacer(
                        modifier = Modifier.width(SMALL_PADDING.dp)
                    )
                }
            }
            HomeHorizontalSpacerLarge()
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMALL_PADDING.dp)
                ) {
                    Spacer(
                        modifier = Modifier.width(LARGE_PADDING.dp)
                    )
                    Text(
                        text = "МОЁ",
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(
                    modifier = Modifier.height(SMALL_PADDING.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                    IconicCardSmall(
                        title = "Хронология",
                        icon = Icons.Filled.History,
                        backgroundTile = ImageBitmap.imageResource(
                            if (isLocalAppDarkThemeEnabled()) {
                                R.drawable.pattern_history_dark
                            } else {
                                R.drawable.pattern_history_light
                            }
                        ),
                        onClick = onNavigateToHistory
                    )
                    Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                    IconicCardSmall(
                        title = "Избранное",
                        icon = Icons.Filled.Favorite,
                        backgroundTile = ImageBitmap.imageResource(
                            if (isLocalAppDarkThemeEnabled()) {
                                R.drawable.pattern_favorite_dark
                            } else {
                                R.drawable.pattern_favorite_light
                            }
                        ),
                        onClick = onNavigateToFavoriteStories
                    )
                    Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                }
            }
            HomeHorizontalSpacerLarge()
        }
    }
}