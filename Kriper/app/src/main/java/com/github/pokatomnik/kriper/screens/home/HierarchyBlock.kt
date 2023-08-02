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
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.Casino
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
fun HierarchyBlock(
    onNavigateToTagGroups: () -> Unit,
    onNavigateToAllTags: () -> Unit,
    onNavigateToRandom: () -> Any
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
                text = "НАВИГАЦИЯ",
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
                title = "Группы меток",
                icon = Icons.Filled.Category,
                backgroundTile = ImageBitmap.imageResource(
                    if (isLocalAppDarkThemeEnabled()) {
                        R.drawable.pattern_tag_group_dark
                    } else {
                        R.drawable.pattern_tag_group_light
                    }
                ),
                onClick = onNavigateToTagGroups
            )
            Spacer(
                modifier = Modifier.width(SMALL_PADDING.dp)
            )
            IconicCardSmall(
                title = "Все метки",
                icon = Icons.Filled.Tag,
                backgroundTile = ImageBitmap.imageResource(
                    if (isLocalAppDarkThemeEnabled()) {
                        R.drawable.pattern_tag_dark
                    } else {
                        R.drawable.pattern_tag_light
                    }
                ),
                onClick = onNavigateToAllTags
            )
            Spacer(
                modifier = Modifier.width(SMALL_PADDING.dp)
            )
            IconicCardSmall(
                title = "Случайная",
                icon = Icons.Outlined.Casino,
                backgroundTile = ImageBitmap.imageResource(
                    if (isLocalAppDarkThemeEnabled()) {
                        R.drawable.pattern_random_dark
                    } else {
                        R.drawable.pattern_random_light
                    }
                ),
                onClick = { onNavigateToRandom() }
            )
            Spacer(
                modifier = Modifier.width(SMALL_PADDING.dp)
            )
        }
    }
}
