package com.github.pokatomnik.kriper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.theme.KriperTheme
import com.github.pokatomnik.kriper.ui.widgets.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KriperTheme {
                IndexServiceReadiness(
                    done = { AppComposable() },
                    wip = { SplashScreen() }
                )
            }
        }
    }
}
