package com.github.pokatomnik.kriper.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ext.uppercaseFirst
import com.github.pokatomnik.kriper.ui.components.*

@Composable
fun StoryCardNavigationListItem(
    title: String,
    tags: Collection<String>,
    rating: Int,
    author: String,
    readingTimeMinutes: Float,
    onClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(ALPHA_GHOST),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = author,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = tags.joinToString(", ") { "#${it.uppercaseFirst()}" },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    VerticalDivider(modifier = Modifier.padding(horizontal = SMALL_PADDING.dp))
                    Column(modifier = Modifier.width(72.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (rating) {
                                    in Int.MIN_VALUE until 0 -> Icons.Filled.HeartBroken
                                    0 -> Icons.Filled.FavoriteBorder
                                    else -> Icons.Filled.Favorite
                                },
                                contentDescription = "Рейтинг",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(" $rating")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Timer,
                                contentDescription = "Время на прочтение",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(" ${readingTimeMinutes.format(1)}")
                        }
                    }
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
