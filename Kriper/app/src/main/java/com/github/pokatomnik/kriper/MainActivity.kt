package com.github.pokatomnik.kriper

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.pokatomnik.kriper.ext.PubSub
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

    private var deeplinkHolder: DeeplinkHolder? = null

    private val deeplinkPubSub = PubSub<Uri?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storyIdHolder = deeplinkHolder ?: DeeplinkHolder(
            initialDeeplink = intent.data
        ).apply { deeplinkHolder = this }

        block.tryInit()
        setContent {
            KriperTheme {
                IndexServiceReadiness(
                    done = {
                        AppComposable(
                            deeplinkHolder = storyIdHolder,
                            deeplinkSubscriber = deeplinkPubSub,
                        )
                    },
                    wip = { SplashScreen() }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        deeplinkPubSub.publish(intent?.data)
    }
}
