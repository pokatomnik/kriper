package com.github.pokatomnik.kriper.ui.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ext.uppercaseFirst
import com.github.pokatomnik.kriper.ui.components.ALPHA_GHOST
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.components.VerticalDivider
import com.github.pokatomnik.kriper.ui.components.format

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoryCardNavigationListItem(
    title: String,
    tags: Collection<String>,
    rating: Int,
    author: String,
    readingTimeMinutes: Float,
    liked: Boolean,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null
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
                .combinedClickable(
                    indication = rememberRipple(bounded = true),
                    onClick = onClick,
                    onLongClick = onLongClick,
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
                    Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.h6,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                    Column(
                        modifier = Modifier.width(72.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        if (liked) {
                            Text(
                                text = "Избранное",
                                style = MaterialTheme.typography.caption,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
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
