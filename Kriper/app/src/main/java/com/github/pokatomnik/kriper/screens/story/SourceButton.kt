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
    colorsInfo: ColorsInfo,
    displayAfter: @Composable () -> Unit,
) {
    val context = LocalContext.current

    IndexServiceReadiness { indexService ->
        indexService.content.getPageMetaByName(pageTitle)?.let { pageMeta ->
            val source = pageMeta.source
            val webpageURL = pageMeta.webpageURL

            val sourceURI = remember(source) {
                source?.let { try { Uri.parse(source) } catch (e: Exception) { null } }
            }

            val handleOpenSourceURL: () -> Unit = {
                sourceURI?.let {
                    val intent = Intent(Intent.ACTION_VIEW, sourceURI)
                    context.startActivity(intent)
                }
            }

            val handleReadOnKriper: () -> Unit = {
                try { Uri.parse(webpageURL) } catch (e: Exception) { null }
                    ?.let { uri ->
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    }
            }

            val renderText = @Composable {
                source?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.alpha(ALPHA_GHOST),
                        color = colorsInfo.contentColor ?: contentColorFor(
                            MaterialTheme.colors.surface
                        )
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "ИСТОЧНИК:",
                        fontWeight = FontWeight.Bold,
                        color = colorsInfo.contentColor ?: contentColorFor(
                            MaterialTheme.colors.surface
                        ),
                        modifier = Modifier.alpha(ALPHA_GHOST),
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (sourceURI != null) {
                        TextButton(onClick = handleOpenSourceURL) {
                            renderText()
                        }
                    } else {
                        renderText()
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = handleReadOnKriper) {
                        Text("Читать на Kriper")
                    }
                }
            }
            displayAfter()
        }
    }
}