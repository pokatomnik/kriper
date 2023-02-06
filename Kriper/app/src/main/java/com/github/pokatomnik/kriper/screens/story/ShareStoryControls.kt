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
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING

@Composable
fun ShareStoryControls(
    storyTitle: String,
) {
    IndexServiceReadiness { indexService ->
        val context = LocalContext.current
        val handleShareClick: () -> Unit = {
            indexService.content.getPageMetaByName(storyTitle)?.let { pageMeta ->
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "История с Kriper.net")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "$storyTitle\n\n${pageMeta.webpageURL}"
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