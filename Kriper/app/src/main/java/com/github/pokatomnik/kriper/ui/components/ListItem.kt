package com.github.pokatomnik.kriper.ui.components

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CardNavigationListItem(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = 3.dp,
        modifier = Modifier.fillMaxWidth().height(96.dp)
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
                .padding(LARGE_PADDING.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
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
                Spacer(modifier = Modifier.fillMaxWidth().height(SMALL_PADDING.dp))
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
                modifier = Modifier.fillMaxHeight(),
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

@Composable
fun OneRowNavigationListItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
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
            modifier = Modifier.height(64.dp),
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