package com.github.pokatomnik.kriper.screens.story

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.domain.PageMeta
import com.github.pokatomnik.kriper.ext.uppercaseFirst
import com.github.pokatomnik.kriper.services.KRIPER_DOMAIN
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING

@Composable
fun ShareStoryControls(
    storyId: String
) {
    IndexServiceReadiness { indexService ->
        val context = LocalContext.current
        val handleShareClick: () -> Unit = {
            indexService.content.getPageMetaByStoryId(storyId)?.let { pageMeta ->
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "История с ${KRIPER_DOMAIN.uppercaseFirst()}")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "${pageMeta.title}\n\n${pageMeta.getShareURL()}"
                    )
                }
                context.startActivity(Intent.createChooser(shareIntent, "Где поделиться"))
            }
        }

        Column(modifier = Modifier.fillMaxWidth().height(32.dp).padding(horizontal = LARGE_PADDING.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Поделиться",
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Row {
                        IconButton(onClick = handleShareClick) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Поделиться историей"
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun PageMeta.getShareURL(): String {
    return "https://$KRIPER_DOMAIN/index.php?newsid=${this.storyId}"
}