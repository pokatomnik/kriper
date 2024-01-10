package com.github.pokatomnik.kriper.ext

val mapping = mapOf(
    1 to "Январь",
    2 to "Февраль",
    3 to "Март",
    4 to "Апрель",
    5 to "Май",
    6 to "Июнь",
    7 to "Июль",
    8 to "Август",
    9 to "Сентябрь",
    10 to "Октябрь",
    11 to "Ноябрь",
    12 to "Декабрь"
)

fun Int.asMonthCyrillic(): String {
    return mapping[this] ?: throw Exception("Month with index \"$this\" is out of requried bounds (0-12)")
}