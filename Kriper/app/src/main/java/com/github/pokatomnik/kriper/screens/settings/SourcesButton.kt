package com.github.pokatomnik.kriper.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun SourcesButton() {
    val context = LocalContext.current
    val openSourceCodePage = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/pokatomnik/kriper"))
        context.startActivity(intent)
    }

    TextButton(onClick = openSourceCodePage) {
        Text("Исходный код")
    }
}