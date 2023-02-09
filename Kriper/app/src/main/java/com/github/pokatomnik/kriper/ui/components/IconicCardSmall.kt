package com.github.pokatomnik.kriper.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun IconicCardSmall(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = 3.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .clickable(
                    indication = rememberRipple(),
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(32.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = SMALL_PADDING.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun IconicCardSmall(
    title: String,
    modifier: Modifier = Modifier,
    backgroundPainter: Painter? = null,
    onClick: () -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = 3.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            backgroundPainter?.let { painter ->
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentDescription = title,
                    contentScale = ContentScale.Fit
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds()
                    .clickable(
                        indication = rememberRipple(),
                        onClick = onClick,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {}
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f)
                        .background(MaterialTheme.colors.background.copy(alpha = ALPHA_GHOST))
                        .padding(SMALL_PADDING.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}