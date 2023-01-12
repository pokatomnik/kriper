package com.github.pokatomnik.kriper.domain

data class UncheckedDate (
    val year: Int,
    val month: Int,
    val day: Int,
) : Comparable<UncheckedDate> {
    override fun compareTo(other: UncheckedDate): Int {
        if (year != other.year) return year - other.year
        if (month != other.month) return month - other.month
        if (day != other.day) return day - other.day
        return 0
    }
}