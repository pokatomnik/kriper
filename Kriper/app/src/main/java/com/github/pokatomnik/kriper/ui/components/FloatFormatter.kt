package com.github.pokatomnik.kriper.ui.components

fun Float.format(digits: Int) = if (this - this.toInt() > 0) {
    "%.${digits}f".format(this).replace(",", ".")
} else {
    this.toInt().toString()
}