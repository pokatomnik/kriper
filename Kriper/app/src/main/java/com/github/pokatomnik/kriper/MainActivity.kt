package com.github.pokatomnik.kriper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.pokatomnik.kriper.services.copyrightblock.CopyrightBlock
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.theme.KriperTheme
import com.github.pokatomnik.kriper.ui.widgets.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var block: CopyrightBlock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        block.tryInit()
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
