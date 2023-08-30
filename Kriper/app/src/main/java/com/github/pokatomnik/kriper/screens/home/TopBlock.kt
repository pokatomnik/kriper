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
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
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
fun TopBlock(
    onNavigateToWeekTop: () -> Unit,
    onNavigateToMonthTop: () -> Unit,
    onNavigateToYearTop: () -> Unit,
    onNavigateToAllTheTimeTop: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SMALL_PADDING.dp)
        ) {
            Spacer(modifier = Modifier.width(LARGE_PADDING.dp))
            Text(
                text = "ТОП",
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(SMALL_PADDING.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
            IconicCardSmall(
                icon = Icons.Filled.DateRange,
                title = "За неделю",
                backgroundTile = ImageBitmap.imageResource(
                    if (isLocalAppDarkThemeEnabled()) {
                        R.drawable.pattern_top_dark
                    } else {
                        R.drawable.pattern_top_light
                    }
                ),
                onClick = onNavigateToWeekTop
            )
            Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
            IconicCardSmall(
                icon = Icons.Filled.CalendarMonth,
                title = "За месяц",
                backgroundTile = ImageBitmap.imageResource(
                    if (isLocalAppDarkThemeEnabled()) {
                        R.drawable.pattern_top_dark
                    } else {
                        R.drawable.pattern_top_light
                    }
                ),
                onClick = onNavigateToMonthTop
            )
            Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
            IconicCardSmall(
                icon = Icons.Filled.CalendarToday,
                title = "За год",
                backgroundTile = ImageBitmap.imageResource(
                    if (isLocalAppDarkThemeEnabled()) {
                        R.drawable.pattern_top_dark
                    } else {
                        R.drawable.pattern_top_light
                    }
                ),
                onClick = onNavigateToYearTop
            )
            Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
            IconicCardSmall(
                icon = Icons.Filled.AllInclusive,
                title = "За все время",
                backgroundTile = ImageBitmap.imageResource(
                    if (isLocalAppDarkThemeEnabled()) {
                        R.drawable.pattern_top_dark
                    } else {
                        R.drawable.pattern_top_light
                    }
                ),
                onClick = onNavigateToAllTheTimeTop
            )
            Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
        }
    }
}