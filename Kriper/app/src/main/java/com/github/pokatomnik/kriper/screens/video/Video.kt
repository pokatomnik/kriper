package com.github.pokatomnik.kriper.screens.video

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

class KriperWebViewClient(
    private val onNavigate: (url: String?) -> Unit,
    private val onPageFinished: () -> Unit
) : AccompanistWebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        onPageFinished()
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        onNavigate(url)
        super.onPageStarted(view, url, favicon)
    }
}

@SuppressLint("SetJavaScriptEnabled", "WebViewApiAvailability")
@Composable
fun VideoInternal(videoURL: String, onNavigate: () -> Unit) {
    val loadingState = remember { mutableStateOf(true) }
    val state = rememberWebViewState(url = videoURL)
    val client = remember(videoURL) {
        KriperWebViewClient(
            onNavigate = { url ->
                if (url != videoURL) {
                    onNavigate()
                }
            },
            onPageFinished = { loadingState.value = false }
        )
    }

    PageContainer {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Загрузка...",
                modifier = Modifier.alpha(if (loadingState.value) 1f else 0f)
            )
            WebView(
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds()
                    .alpha(if (loadingState.value) 0f else 1f),
                onCreated = { it.settings.javaScriptEnabled = true },
                client = client
            )
        }
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