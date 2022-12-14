package com.github.pokatomnik.kriper.ext

fun String.uppercaseFirst(): String {
    return if (this.isEmpty()) this else this[0].uppercase() + this.substring(1)
}