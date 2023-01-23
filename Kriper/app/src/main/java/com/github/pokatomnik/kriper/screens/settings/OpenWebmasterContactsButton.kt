package com.github.pokatomnik.kriper.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun OpenWebmasterContactsButton() {
    val context = LocalContext.current
    val handleOpenWebmasterContactsClick = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://kriper.net/index.php?do=feedback"))
        context.startActivity(intent)
    }

    TextButton(onClick = handleOpenWebmasterContactsClick) {
        Text("Написать вебмастеру")
    }
}