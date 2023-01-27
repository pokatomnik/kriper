package com.github.pokatomnik.kriper.screens.story

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.services.preferences.page.ColorsInfo
import com.github.pokatomnik.kriper.ui.components.ALPHA_GHOST

@Composable
fun SourceButton(
    pageTitle: String,
    colorsInfo: ColorsInfo
) {
    val context = LocalContext.current

    IndexServiceReadiness { indexService ->
        val pageMeta = indexService.content.getPageMetaByName(pageTitle)
        pageMeta?.source?.let { source ->
            val uri = remember(source) {
                try { Uri.parse(source) } catch (e: Exception) { null }
            }

            val handleClick: () -> Unit = {
                uri?.let {
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }
            }

            val renderText = @Composable {
                Text(
                    text = source,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alpha(ALPHA_GHOST),
                    color = colorsInfo.contentColor ?: contentColorFor(
                        MaterialTheme.colors.surface
                    )
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Источник:",
                        fontWeight = FontWeight.Bold,
                        color = colorsInfo.contentColor ?: contentColorFor(
                            MaterialTheme.colors.surface
                        ),
                        modifier = Modifier.alpha(ALPHA_GHOST),
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (uri != null) {
                        TextButton(onClick = handleClick) {
                            renderText()
                        }
                    } else {
                        renderText()
                    }
                }
            }
        }
    }
}