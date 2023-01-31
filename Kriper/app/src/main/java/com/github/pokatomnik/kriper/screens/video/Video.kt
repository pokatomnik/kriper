package com.github.pokatomnik.kriper.screens.video

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

class KriperWebViewClient(
    private val onNavigate: (view: WebView?, url: String?, favicon: Bitmap?) -> Unit
) : AccompanistWebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        onNavigate(view, url, favicon)
        super.onPageStarted(view, url, favicon)
    }
}

@SuppressLint("SetJavaScriptEnabled", "WebViewApiAvailability")
@Composable
fun VideoInternal(videoURL: String, onNavigate: () -> Unit) {
    val state = rememberWebViewState(url = videoURL)
    val client = remember(videoURL) {
        KriperWebViewClient { _, url, _ ->
            if (url != videoURL) {
                onNavigate()
            }
        }
    }

    PageContainer {
        WebView(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds(),
            onCreated = { it.settings.javaScriptEnabled = true },
            client = client
        )
    }
}

@Composable
fun Video(videoURL: String) {
    val videoKeyState = remember { mutableStateOf(0) }
    key(videoKeyState.value) {
        VideoInternal(
            videoURL = videoURL,
            onNavigate = { ++videoKeyState.value }
        )
    }
}