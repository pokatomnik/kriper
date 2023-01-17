package com.github.pokatomnik.kriper.ui.components

import androidx.annotation.FontRes
import androidx.annotation.IdRes
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import coil.ImageLoader
import dev.jeziellago.compose.markdowntext.MarkdownText as MarkdownTextComponent

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    @FontRes fontResource: Int? = null,
    style: TextStyle = LocalTextStyle.current,
    @IdRes viewId: Int? = null,
    onClick: (() -> Unit)? = null,
    disableLinkMovementMethod: Boolean = false,
    imageLoader: ImageLoader? = null,
) {
    return key(fontSize, fontResource) {
        MarkdownTextComponent(
            markdown = markdown,
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            textAlign = textAlign,
            maxLines = maxLines,
            fontResource = fontResource,
            style = style,
            viewId = viewId,
            onClick = onClick,
            disableLinkMovementMethod = disableLinkMovementMethod,
            imageLoader = imageLoader
        )
    }
}