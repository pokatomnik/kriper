package com.github.pokatomnik.kriper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

const val MEDIUM_ITEM_HEIGHT = 64
const val LARGE_ITEM_HEIGHT = 96

@Composable
fun OneRowNavigationListItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(MEDIUM_ITEM_HEIGHT.dp)
            .clickable(
                indication = rememberRipple(bounded = true),
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(horizontal = LARGE_PADDING.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            modifier = Modifier.height(MEDIUM_ITEM_HEIGHT.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
               imageVector = Icons.Filled.ChevronRight,
               contentDescription = "Перейти к $title"
            )
        }
    }
}