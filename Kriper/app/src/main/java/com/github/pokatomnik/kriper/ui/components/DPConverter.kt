package com.github.pokatomnik.kriper.ui.components

import android.content.Context
import android.util.DisplayMetrics

fun Float.asPixelsToDp(context: Context): Float {
    val displayMetrics = context.resources.displayMetrics
    return (this / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

fun Int.asDpToPixels(context: Context): Float {
    val displayMetrics = context.resources.displayMetrics
    return (this * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}