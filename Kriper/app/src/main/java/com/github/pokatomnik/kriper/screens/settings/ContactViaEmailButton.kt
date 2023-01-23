package com.github.pokatomnik.kriper.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ContactViaEmailButton() {
    val context = LocalContext.current
    val handleWriteEmailClick = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:pokatomnik@yandex.ru"))
        context.startActivity(intent)
    }

    TextButton(onClick = handleWriteEmailClick) {
        Text("Написать разработчику")
    }
}