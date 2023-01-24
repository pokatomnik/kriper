package com.github.pokatomnik.kriper.ext

private val valuableChars = "abcdefghijklmnopqrstuvwxyzабвгдеёжзиклмнопрстуфхцчшщъыьэюя"
    .let { it + it.uppercase() } + "0123456789".split("").toSet()

fun String.valuableChars(): String = this.filter { char -> valuableChars.contains(char) }