package com.github.pokatomnik.kriper.screens.video

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled", "WebViewApiAvailability")
@Composable
fun Video(videoURL: String) {
    val uriWithNoControls = remember(videoURL) {
        val originalUri = Uri.parse(videoURL)
        originalUri.buildUpon().appendQueryParameter("controls", "0").build().toString()
    }
    val state = rememberWebViewState(url = uriWithNoControls)
    PageContainer {
        WebView(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds(),
            onCreated = {
                it.settings.javaScriptEnabled = true
            },
        )
    }
}