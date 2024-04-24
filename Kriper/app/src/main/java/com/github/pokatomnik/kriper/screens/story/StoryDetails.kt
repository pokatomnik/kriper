package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.services.preferences.page.ColorsInfo
import com.github.pokatomnik.kriper.ui.components.ALPHA_GHOST
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.github.pokatomnik.kriper.ui.components.format
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment

@Composable
fun StoryDetails(
    storyId: String,
    colorsInfo: ColorsInfo,
    onNavigateToAuthor: (authorRealName: String) -> Unit,
    displayAfter: @Composable () -> Unit,
) {
    IndexServiceReadiness { indexService ->
        indexService.content.getPageMetaByStoryId(storyId)?.let { pageMeta ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (pageMeta.authorRealName != null) {
                        TextButton(onClick = { onNavigateToAuthor(pageMeta.authorRealName) }) {
                            Text(
                                text = "© ${pageMeta.authorRealName}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = colorsInfo.contentColor ?: contentColorFor(
                                    MaterialTheme.colors.surface
                                ),
                                modifier = Modifier.alpha(ALPHA_GHOST)
                            )
                        }
                    } else {
                        Text(
                            text = "Добавил(а) ${pageMeta.authorNickname}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = colorsInfo.contentColor ?: contentColorFor(
                                MaterialTheme.colors.surface
                            ),
                            modifier = Modifier.alpha(ALPHA_GHOST)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(SMALL_PADDING.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    mainAxisAlignment = MainAxisAlignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when (pageMeta.rating) {
                                in Int.MIN_VALUE until 0 -> Icons.Filled.HeartBroken
                                0 -> Icons.Filled.FavoriteBorder
                                else -> Icons.Filled.Favorite
                            },
                            contentDescription = "Рейтинг",
                            modifier = Modifier
                                .size(18.dp)
                                .alpha(ALPHA_GHOST),
                            tint = colorsInfo.contentColor ?: contentColorFor(MaterialTheme.colors.surface)
                        )
                        Text(
                            text = " ${pageMeta.rating}",
                            color = colorsInfo.contentColor ?: contentColorFor(
                                MaterialTheme.colors.surface
                            ),
                            modifier = Modifier.alpha(ALPHA_GHOST)
                        )
                    }
                    Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = "Время на прочтение",
                            modifier = Modifier
                                .size(18.dp)
                                .alpha(ALPHA_GHOST),
                            tint = colorsInfo.contentColor ?: contentColorFor(
                                MaterialTheme.colors.surface
                            )
                        )
                        Text(
                            text = " ${pageMeta.readingTimeMinutes.format(1)}",
                            color = colorsInfo.contentColor ?: contentColorFor(
                                MaterialTheme.colors.surface
                            ),
                            modifier = Modifier.alpha(ALPHA_GHOST)
                        )
                    }
                }
            }
            displayAfter()
        }
    }
}