package com.github.pokatomnik.kriper.screens.story

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.github.pokatomnik.kriper.ext.getPluralNoun
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.services.preferences.page.ColorsInfo
import com.github.pokatomnik.kriper.ui.components.ALPHA_GHOST

@Composable
fun GalleryButton(
    pageTitle: String,
    colorsInfo: ColorsInfo,
    onNavigateToGallery: () -> Unit,
    displayAfter: @Composable () -> Unit,
) {
    IndexServiceReadiness { indexService ->
        indexService.content.getPageMetaByName(pageTitle)?.let { pageMeta ->
            val numberOfImages = pageMeta.images.size
            val storiesPlural = numberOfImages.getPluralNoun(
                form1 = "картинка",
                form2 = "картинки",
                form3 = "картинок"
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "ГАЛЕРЕЯ",
                        fontWeight = FontWeight.Bold,
                        color = colorsInfo.contentColor ?: contentColorFor(
                            MaterialTheme.colors.surface
                        ),
                        modifier = Modifier.alpha(ALPHA_GHOST)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onNavigateToGallery) {
                        Text(
                            text = "Смотреть $numberOfImages $storiesPlural",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.alpha(ALPHA_GHOST),
                            color = colorsInfo.contentColor ?: contentColorFor(
                                MaterialTheme.colors.surface
                            )
                        )
                    }
                }
            }
            displayAfter()
        }
    }
}