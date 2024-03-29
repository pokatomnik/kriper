package com.github.pokatomnik.kriper.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

private const val CARD_HEIGHT = 96

@Composable
fun CardNavigationListItem(
    title: String,
    description: String,
    iconPainter: Painter? = null,
    onClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(CARD_HEIGHT.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .clickable(
                    indication = rememberRipple(bounded = true),
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            iconPainter?.let { iconPainter ->
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(CARD_HEIGHT.dp),
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = iconPainter,
                        contentDescription = "Изображение $title",
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Spacer(modifier = Modifier.width(LARGE_PADDING.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(
                        top = LARGE_PADDING.dp,
                        bottom = LARGE_PADDING.dp
                    ),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(SMALL_PADDING.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = description,
                        modifier = Modifier.alpha(ALPHA_GHOST),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight().padding(end = LARGE_PADDING.dp),
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
}
