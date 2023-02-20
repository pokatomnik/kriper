package com.github.pokatomnik.kriper.ui.components

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconicCardFull(
    title: String,
    icon: ImageVector,
    description: String,
    backgroundTile: ImageBitmap? = null,
    onClick: () -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().let { modifier ->
                backgroundTile?.let { imageBitmap ->
                    remember(imageBitmap) {
                        ShaderBrush(ImageShader(imageBitmap, TileMode.Repeated, TileMode.Repeated))
                    }.let { modifier.background(it) }
                } ?: modifier
            }
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
                    .padding(
                        top = LARGE_PADDING.dp,
                        end = LARGE_PADDING.dp,
                        bottom = LARGE_PADDING.dp
                    )
            ) {
                Column(
                    modifier = Modifier.size(width = 64.dp, height = 128.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(32.dp)
                    )
                }
                VerticalDivider()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(LARGE_PADDING.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h6,
                    )
                    Text(
                        text = description,
                        modifier = Modifier.alpha(ALPHA_GHOST)
                    )
                }
            }
        }
    }
}