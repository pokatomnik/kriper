package com.github.pokatomnik.kriper.services.db.dao.favoritestories

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences
import com.github.pokatomnik.kriper.ui.widgets.LocalScaffoldState
import kotlinx.coroutines.launch

@Composable
fun DisplayHelpSideEffect() {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = LocalScaffoldState.current
    rememberPreferences().globalPreferences.oneTimeRunners.oncePerInstallActions
        .Once("SWIPE_HELP") {
            scaffoldState?.let {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Свайп влево - добавить/убрать из избранного",
                        actionLabel = "Хорошо",
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }
        }
}