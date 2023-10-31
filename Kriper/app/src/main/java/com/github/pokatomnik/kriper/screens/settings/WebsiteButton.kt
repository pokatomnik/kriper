package com.github.pokatomnik.kriper.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.github.pokatomnik.kriper.services.KRIPER_DOMAIN

@Composable
fun WebsiteButton() {
    val context = LocalContext.current
    val openWebsite = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://$KRIPER_DOMAIN"))
        context.startActivity(intent)
    }

    TextButton(onClick = openWebsite) {
        Text("Открыть сайт")
    }
}