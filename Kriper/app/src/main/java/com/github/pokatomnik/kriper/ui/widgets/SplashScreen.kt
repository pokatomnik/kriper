package com.github.pokatomnik.kriper.ui.widgets

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import com.github.pokatomnik.kriper.R

@Composable
fun SplashScreen() {
    val configuration = LocalConfiguration.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(
                    id = when (configuration.orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> R.drawable.splash_landscape
                        Configuration.ORIENTATION_PORTRAIT -> R.drawable.splash_portrait
                        else -> R.drawable.splash_undefined
                    }
                ),
                contentDescription = "Kriper Logo",
                contentScale = ContentScale.Crop
            )
        }
    }
}