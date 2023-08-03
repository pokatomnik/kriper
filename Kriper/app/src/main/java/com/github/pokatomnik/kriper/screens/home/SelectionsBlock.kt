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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FiberNew
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.ReceiptLong
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
fun SelectionsBlock(
    onNavigateToAllStories: () -> Unit,
    onNavigateToNewStories: () -> Unit,
    onNavigateToShortStories: () -> Unit,
    onNavigateToLongStories: () -> Unit,
    onNavigateToGoldStories: () -> Unit,
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
            IconicCardSmall(
                icon = Icons.Filled.EmojiEvents,
                title = "Золотой фонд",
                backgroundTile = ImageBitmap.imageResource(
                    if (isLocalAppDarkThemeEnabled()) {
                        R.drawable.pattern_gold_dark
                    } else {
                        R.drawable.pattern_gold_light
                    }
                ),
                onClick = onNavigateToGoldStories
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
        }
    }
}