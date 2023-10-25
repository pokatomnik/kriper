package com.github.pokatomnik.kriper.ui.components

fun String.embraceWithQuotationMarks(): String {
    val start = "«"
    val end = "»"
    val nameWithoutQuotes = this.removePrefix(start).removeSuffix(end)
    return "$start$nameWithoutQuotes$end"
}