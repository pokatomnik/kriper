package com.github.pokatomnik.kriper.screens.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Task
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.R
import com.github.pokatomnik.kriper.ui.components.IconicCardSmall
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.theme.isLocalAppDarkThemeEnabled

@Composable
fun PersonalBlock(
    onNavigateToHistory: () -> Unit,
    onNavigateToFavoriteStories: () -> Unit,
    onNavigateToReadStories: () -> Unit,
) {
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
            IconicCardSmall(
                icon = Icons.Filled.Task,
                title = "Прочитанные",
                backgroundTile = ImageBitmap.imageResource(
                    if (isLocalAppDarkThemeEnabled()) {
                        R.drawable.pattern_read_stories_dark
                    } else {
                        R.drawable.pattern_read_stories_light
                    }
                ),
                onClick = onNavigateToReadStories
            )
            Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
        }
    }
}