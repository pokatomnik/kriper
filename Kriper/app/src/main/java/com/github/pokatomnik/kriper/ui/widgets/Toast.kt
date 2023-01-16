package com.github.pokatomnik.kriper.ui.widgets

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.github.pokatomnik.kriper.services.preferences.rememberPreferences

@Composable
fun ShowToastSideEffect(message: String, duration: Int = Toast.LENGTH_LONG) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Toast.makeText(context, message, duration).show()
    }
}

@Composable
fun ShowToastOncePerInstallSideEffect(message: String, duration: Int = Toast.LENGTH_LONG) {
    val context = LocalContext.current
    val oncePerInstallActions = rememberPreferences().globalPreferences.oneTimeRunners.oncePerInstallActions
    oncePerInstallActions.Once(
        key = message,
        action = {
            Toast.makeText(context, message, duration).show()
        }
    )
}

@Composable
fun ShowToastOncePerRunSideEffect(message: String, duration: Int = Toast.LENGTH_LONG) {
    val context = LocalContext.current
    val oncePerRunActions = rememberPreferences().globalPreferences.oneTimeRunners.oncePerRunActions
    oncePerRunActions.Once(
        key = message,
        action = {
            Toast.makeText(context, message, duration).show()
        }
    )
}