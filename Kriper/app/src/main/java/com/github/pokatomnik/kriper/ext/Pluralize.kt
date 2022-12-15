package com.github.pokatomnik.kriper.ext

// Dirty port of https://github.com/megatolya/plural-ru

fun Int.getPluralNoun(form1: String, form2: String, form3: String): String {
    return if (this % 10 == 1 && this % 100 != 11) {
        form1
    } else if (this % 10 in 2..4 && (this % 100 < 10 || this % 100 >= 20)) {
        form2
    } else {
        form3
    }
}
